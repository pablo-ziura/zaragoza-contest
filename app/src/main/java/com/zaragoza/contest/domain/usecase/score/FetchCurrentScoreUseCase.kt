package com.zaragoza.contest.domain.usecase.score

import com.zaragoza.contest.domain.ScorePreferences

class FetchCurrentScoreUseCase(private val scorePreferences: ScorePreferences) {

    fun execute(): Int {
        return scorePreferences.fetchCurrentScore()
    }
}