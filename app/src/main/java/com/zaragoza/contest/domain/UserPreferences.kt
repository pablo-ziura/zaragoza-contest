package com.zaragoza.contest.domain

interface UserPreferences {
    fun saveUserId(userId: String)
    fun fetchUserId(): String?
}

