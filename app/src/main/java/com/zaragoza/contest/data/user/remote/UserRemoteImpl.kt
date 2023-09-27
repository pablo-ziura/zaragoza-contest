package com.zaragoza.contest.data.user.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zaragoza.contest.domain.model.User
import java.util.concurrent.CountDownLatch

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

    fun checkUser(userEmail: String, userPassword: String) {
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser

                    Log.i("USER SIGN IN", "signInWithEmail:success")

                } else {
                    val reason = task.exception?.message
                    Log.i("USER SIGN IN", "ERROR: $reason")
                }
            }
    }

    fun getUserInfo(userId: String): User {
        // Definir una variable mutable para almacenar el usuario
        var user: User? = null

        // Crear una referencia a la ubicación del usuario en la base de datos
        val userRef = database.getReference("Users").child(userId)

        // Sincronizar la lectura de datos
        val sync = CountDownLatch(1)

        // Agregar un listener para un solo evento a userRef
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Obtener el valor de dataSnapshot como un objeto User
                user = dataSnapshot.getValue(User::class.java)
                // Decrementar el contador del latch, permitiendo que la función continúe
                sync.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("USER INFO", "getUserInfo:onCancelled", databaseError.toException())
                // Decrementar el contador del latch, permitiendo que la función continúe
                sync.countDown()
            }
        })

        // Esperar hasta que se reciban los datos o se cancele la lectura
        sync.await()

        // Si user es null, lanzar una excepción o retornar un usuario predeterminado
        return user
            ?: throw NoSuchElementException("No se pudo encontrar el usuario con ID: $userId")
    }
}
