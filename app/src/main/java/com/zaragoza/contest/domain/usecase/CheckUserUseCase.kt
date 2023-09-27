package com.zaragoza.contest.domain.usecase

import com.zaragoza.contest.domain.UserRepository

class CheckUserUseCase(private val userRepository: UserRepository) {

    fun execute(userEmail: String, userPassword: String) {
        userRepository.checkUser(userEmail, userPassword)
    }
}
