package com.zaragoza.contest.data.score.storage

import android.content.Context
import com.zaragoza.contest.domain.ScorePreferences

class ScorePreferencesManager(context: Context) : ScorePreferences {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    override fun updateCurrentUserScore(score: Int) {
        sharedPreferences.edit().putInt("currentUserScore", score).apply()
    }

    override fun fetchCurrentScore(): Int {
        return sharedPreferences.getInt("currentUserScore", 0)
    }

}