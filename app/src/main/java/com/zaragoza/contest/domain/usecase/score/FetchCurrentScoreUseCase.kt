package com.zaragoza.contest.domain.usecase.score

import com.zaragoza.contest.domain.ScoreRepository

class FetchCurrentScoreUseCase(private val scoreRepository: ScoreRepository) {

    fun execute(): Int {
        return scoreRepository.fetchCurrentScore()
    }
}