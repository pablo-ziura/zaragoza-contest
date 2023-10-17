package com.zaragoza.contest.domain.usecase.user

import com.zaragoza.contest.domain.UserPreferences

class FetchUserIdUseCase(private val userPreferences: UserPreferences) {
    fun execute(): String? {
        return userPreferences.fetchUserId()
    }
}