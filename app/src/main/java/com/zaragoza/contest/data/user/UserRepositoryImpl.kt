package com.zaragoza.contest.data.user

import com.zaragoza.contest.data.user.remote.UserRemoteImpl
import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.domain.model.User

class UserRepositoryImpl(private val userRemote: UserRemoteImpl) : UserRepository {

    override suspend fun createUser(user: User) {
        userRemote.createUser(user)
    }

    override suspend fun checkUser(userEmail: String, userPassword: String) {
        userRemote.checkUser(userEmail, userPassword)
    }

    override suspend fun getUserInfo(userId: String): User? {
        return userRemote.getUserInfo(userId)
    }
}
