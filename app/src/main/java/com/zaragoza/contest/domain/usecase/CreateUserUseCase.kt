package com.zaragoza.contest.domain.usecase

import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.domain.model.User

class CreateUserUseCase(private val userRepository: UserRepository) {

    fun execute(user: User) {
        userRepository.createUser(user)
    }
}