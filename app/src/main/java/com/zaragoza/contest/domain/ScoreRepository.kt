package com.zaragoza.contest.domain

import com.zaragoza.contest.model.Score

interface ScoreRepository {

    fun updateCurrentUserScore(score: Int)
    fun fetchCurrentScore(): Int
    suspend fun getBestScoresList(): List<Score>

}