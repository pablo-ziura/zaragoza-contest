package com.zaragoza.contest.data.score

import com.zaragoza.contest.data.score.remote.ScoreRemoteImpl
import com.zaragoza.contest.data.score.storage.ScorePreferencesManager
import com.zaragoza.contest.domain.ScoreRepository
import com.zaragoza.contest.model.Score

class ScoreDataRepository(
    private val scorePreferences: ScorePreferencesManager,
    private val scoreRemote: ScoreRemoteImpl
) : ScoreRepository {

    override fun fetchCurrentScore(): Int {
        return scorePreferences.fetchCurrentScore()
    }

    override fun updateCurrentUserScore(score: Int) {
        return scorePreferences.updateCurrentUserScore(score)
    }

    override suspend fun getBestScoresList(): List<Score> {
        return scoreRemote.getBestScoresList()
    }
}