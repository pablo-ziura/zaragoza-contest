package com.zaragoza.contest.domain.usecase.user

import com.zaragoza.contest.domain.UserRepository

class CheckUserUseCase(private val userRepository: UserRepository) {

    suspend fun execute(userEmail: String, userPassword: String) {
        userRepository.checkUser(userEmail, userPassword)
    }
}
