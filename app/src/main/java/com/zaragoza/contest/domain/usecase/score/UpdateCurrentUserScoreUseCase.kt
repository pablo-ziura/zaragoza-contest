package com.zaragoza.contest.domain.usecase.score

import com.zaragoza.contest.domain.ScoreRepository

class UpdateCurrentUserScoreUseCase(private val scoreRepository: ScoreRepository) {
    fun execute(newScore: Int) {
        val currentScore = scoreRepository.fetchCurrentScore()
        val updatedScore = currentScore + newScore
        scoreRepository.updateCurrentUserScore(updatedScore)
    }
}