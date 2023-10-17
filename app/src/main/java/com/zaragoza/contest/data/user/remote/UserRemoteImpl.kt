package com.zaragoza.contest.data.user.remote

import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zaragoza.contest.model.User
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRemoteImpl(private val firebaseAuth: FirebaseAuth) {
    private val database =
        FirebaseDatabase.getInstance("https://zaragoza-contest-default-rtdb.europe-west1.firebasedatabase.app/")

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
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).await()
            val user = result.user
            return user?.uid
        } catch (e: Exception) {
            Log.i("USER SIGN IN", "ERROR: ${e.message}")
            return null
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
}

