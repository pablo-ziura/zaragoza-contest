package com.zaragoza.contest.data.score.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zaragoza.contest.model.Score
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ScoreRemoteImpl {

    private val database =
        FirebaseDatabase.getInstance("https://zaragoza-contest-default-rtdb.europe-west1.firebasedatabase.app/")

    suspend fun getBestScoresList(): List<Score> = suspendCoroutine { continuation ->
        val usersRef = database.getReference("Users")

        val scoresList = mutableListOf<Score>()

        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val nickname = userSnapshot.child("nickname").getValue(String::class.java) ?: ""
                    val scorePoints = userSnapshot.child("score").getValue(Int::class.java) ?: 0
                    val score = Score(nickname, scorePoints)
                    scoresList.add(score)
                }

                scoresList.sortByDescending { it.scorePoints }

                continuation.resume(scoresList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                continuation.resumeWithException(databaseError.toException())
            }
        })
    }

}