package com.zaragoza.contest.domain.usecase.user

import com.zaragoza.contest.domain.UserRepository

class FetchUserIdUseCase(private val userRepository: UserRepository) {
    fun execute(): String? {
        return userRepository.fetchUserId()
    }
}