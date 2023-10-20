package com.zaragoza.contest.domain.usecase.user

import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.model.User

class EditUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(user: User) {
        userRepository.editUser(user)
    }
}