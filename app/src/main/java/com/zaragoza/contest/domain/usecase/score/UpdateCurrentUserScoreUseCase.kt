package com.zaragoza.contest.domain.usecase.score

import com.zaragoza.contest.domain.ScorePreferences

class UpdateCurrentUserScoreUseCase(private val scorePreferences: ScorePreferences) {
    fun execute(newScore: Int) {
        val currentScore = scorePreferences.fetchCurrentScore()
        val updatedScore = currentScore + newScore
        scorePreferences.updateCurrentUserScore(updatedScore)
    }
}