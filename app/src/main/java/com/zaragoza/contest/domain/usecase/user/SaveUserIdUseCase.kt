package com.zaragoza.contest.domain.usecase.user

import com.zaragoza.contest.domain.UserPreferences

class SaveUserIdUseCase(private val userPreferences: UserPreferences) {
    fun execute(userId: String) {
        userPreferences.saveUserId(userId)
    }
}