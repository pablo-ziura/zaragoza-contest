package com.zaragoza.contest.data.score.storage

import android.content.Context

class ScorePreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun updateCurrentUserScore(score: Int) {
        sharedPreferences.edit().putInt("currentUserScore", score).apply()
    }

    fun fetchCurrentScore(): Int {
        return sharedPreferences.getInt("currentUserScore", 0)
    }

}