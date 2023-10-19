package com.zaragoza.contest.data.score

import com.zaragoza.contest.domain.ScorePreferences

class ScoreDataRepository(private val scorePreferences: ScorePreferences) {

    fun fetchCurrentScore(): Int {
        return scorePreferences.fetchCurrentScore()
    }

    fun updateCurrentUserScore(score: Int) {
        return scorePreferences.updateCurrentUserScore(score)
    }

}