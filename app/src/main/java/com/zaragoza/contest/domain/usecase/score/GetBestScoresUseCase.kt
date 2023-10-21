package com.zaragoza.contest.domain.usecase.score

import com.zaragoza.contest.domain.ScoreRepository
import com.zaragoza.contest.model.Score

class GetBestScoresUseCase(
    private val scoreRepository: ScoreRepository
) {
    suspend fun execute(): List<Score> {
        return scoreRepository.getBestScoresList()
    }
}