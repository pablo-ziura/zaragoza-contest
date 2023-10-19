package com.zaragoza.contest.domain

interface ScorePreferences {

    fun updateCurrentUserScore(score: Int)

    fun fetchCurrentScore(): Int

}