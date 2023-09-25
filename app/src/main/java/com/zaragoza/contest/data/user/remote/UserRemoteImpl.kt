package com.zaragoza.contest.data.user.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zaragoza.contest.domain.model.User

class UserRemoteImpl(private val firebaseAuth: FirebaseAuth) {
    private val database =
        FirebaseDatabase.getInstance("https://zaragoza-contest-default-rtdb.europe-west1.firebasedatabase.app/")

    fun createUser(user: User) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                    user.id = uid
                    val userRef = database.getReference("Users").child(user.email)

                    userRef.setValue(user).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            Log.i("USER CREATION", "Usuario creado en Auth y Realtime Database")
                        } else {
                            Log.i(
                                "USER CREATION",
                                "ERROR: No se pudo almacenar datos adicionales en Realtime Database"
                            )
                        }
                    }
                } else {
                    val reason = task.exception?.message
                    Log.i("USER CREATION", "ERROR: $reason")
                }
            }
    }
}
