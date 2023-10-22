package com.zaragoza.contest.data.user.remote

import android.net.Uri
import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import com.zaragoza.contest.model.User
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRemoteImpl(private val firebaseAuth: FirebaseAuth) {
    private val database =
        FirebaseDatabase.getInstance("https://zaragoza-contest-default-rtdb.europe-west1.firebasedatabase.app/")

    private val storageDatabaseRef = Firebase.storage.reference


    suspend fun createUser(user: User) {
        try {
            val result =
                firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            val uid = result.user?.uid ?: throw IllegalStateException("UID no puede ser null")
            user.id = uid
            val bcryptHash = BCrypt.withDefaults().hashToString(12, user.password.toCharArray())
            user.password = bcryptHash
            val userRef = database.getReference("Users").child(uid)
            userRef.setValue(user).await()
        } catch (e: Exception) {
            Log.i("USER CREATION", "ERROR: ${e.message}")
        }
    }

    suspend fun checkUser(userEmail: String, userPassword: String): String? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).await()
            val user = result.user
            user?.uid
        } catch (e: Exception) {
            Log.i("USER SIGN IN", "ERROR: ${e.message}")
            null
        }
    }

    suspend fun getUserInfo(userId: String): User? = suspendCoroutine { continuation ->
        val userRef = database.getReference("Users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                continuation.resume(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("USER INFO", "getUserInfo:onCancelled", databaseError.toException())
                continuation.resume(null)
            }
        })
    }

    suspend fun editUser(user: User) {
        try {
            val uid = user.id
            val userRef = database.getReference("Users").child(uid)
            userRef.setValue(user).await()
        } catch (e: Exception) {
            Log.i("USER EDIT", "ERROR: ${e.message}")
        }
    }

    suspend fun uploadProfileImage(user: User) {
        try {
            val imageRef = storageDatabaseRef.child("profileImgUser/${user.id}.jpg")
            val uri = Uri.parse(user.urlImage)
            val uploadTask = imageRef.putFile(uri)
            uploadTask.await()

            val downloadUri = imageRef.downloadUrl.await()

            user.urlImage = downloadUri.toString()

            editUser(user)
        } catch (e: StorageException) {
            Log.e("UPLOAD IMAGE", "StorageException: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e("UPLOAD IMAGE", "ERROR: ${e.message}")
            throw e
        }
    }

}