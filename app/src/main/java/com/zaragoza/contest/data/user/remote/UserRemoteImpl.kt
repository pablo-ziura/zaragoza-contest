package com.zaragoza.contest.data.user.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.zaragoza.contest.model.User

class UserRemoteImpl(private val firebaseAuth: FirebaseAuth) {
    fun createUser(user: User) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("USER CREATION", "Usuario creado")
                } else {
                    Log.i("USER CREATION", "ERROR: Usuario creado")
                }
            }
    }
}