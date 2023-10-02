package com.zaragoza.contest.domain.usecase.user

import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.domain.model.User

class GetUserInfoUseCase(private val userRepository: UserRepository) {
    suspend fun execute(userId: String): User? {
        return userRepository.getUserInfo(userId)
    }
}