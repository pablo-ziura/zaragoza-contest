package com.zaragoza.contest.domain.usecase

import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.domain.model.User

class GetUserInfoUseCase(private val userRepository: UserRepository) {
    fun execute(userId: String): User {
        return userRepository.getUserInfo(userId)
    }
}