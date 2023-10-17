package com.zaragoza.contest.data.user

import com.zaragoza.contest.data.user.remote.UserRemoteImpl
import com.zaragoza.contest.domain.UserPreferences
import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.model.User

class UserDataRepository(
    private val userRemote: UserRemoteImpl,
    private val userPreferences: UserPreferences
) : UserRepository {

    override suspend fun createUser(user: User) {
        userRemote.createUser(user)
    }

    override suspend fun checkUser(userEmail: String, userPassword: String): String? {
        return userRemote.checkUser(userEmail, userPassword)
    }

    override suspend fun getUserInfo(userId: String): User? {
        return userRemote.getUserInfo(userId)
    }

    fun saveUserId(userId: String) {
        userPreferences.saveUserId(userId)
    }

    fun fetchUserId(): String? {
        return userPreferences.fetchUserId()
    }
}