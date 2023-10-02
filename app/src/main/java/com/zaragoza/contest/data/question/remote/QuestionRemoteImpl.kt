package com.zaragoza.contest.data.question.remote

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zaragoza.contest.domain.model.Question
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QuestionRemoteImpl {

    private val database =
        FirebaseDatabase.getInstance("https://zaragoza-contest-default-rtdb.europe-west1.firebasedatabase.app/")

    suspend fun getQuestionList(): List<Question> = suspendCoroutine { continuation ->
        val questionRef = database.getReference("Questions")
        questionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val questions = dataSnapshot.getValue(listOf<Question>()::class.java)
                continuation.resume(questions)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("USER INFO", "getUserInfo:onCancelled", databaseError.toException())
                continuation.resume(null)
            }
        })
    }

}