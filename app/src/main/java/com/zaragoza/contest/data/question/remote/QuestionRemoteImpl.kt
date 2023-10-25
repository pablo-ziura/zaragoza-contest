package com.zaragoza.contest.data.question.remote

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zaragoza.contest.data.Constants
import com.zaragoza.contest.model.Question
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QuestionRemoteImpl {

    private val database: FirebaseDatabase =
        FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)

    suspend fun getQuestionList(): List<Question> = suspendCoroutine { continuation ->
        val questionRef = database.getReference("Questions")
        questionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val questions = mutableListOf<Question>()

                dataSnapshot.children.forEach { questionSnapshot ->
                    val question = questionSnapshot.getValue(Question::class.java)
                    if (question != null) {
                        questions.add(question)
                    }
                }

                continuation.resume(questions)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("USER INFO", "getUserInfo:onCancelled", databaseError.toException())
                continuation.resume(emptyList())
            }
        })
    }
}