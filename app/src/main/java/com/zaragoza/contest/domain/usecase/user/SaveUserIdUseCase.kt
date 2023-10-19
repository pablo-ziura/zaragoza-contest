package com.zaragoza.contest.domain.usecase.user

import com.zaragoza.contest.domain.UserRepository

class SaveUserIdUseCase(private val userRepository: UserRepository) {
    fun execute(userId: String) {
        userRepository.saveUserId(userId)
    }
}