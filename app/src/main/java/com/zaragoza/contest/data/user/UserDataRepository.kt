package com.zaragoza.contest.data.user

import com.zaragoza.contest.data.user.remote.UserRemoteImpl
import com.zaragoza.contest.data.user.storage.UserPreferencesManager
import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.model.User

class UserDataRepository(
    private val userRemote: UserRemoteImpl,
    private val userPreferences: UserPreferencesManager
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

    override suspend fun editUser(user: User) {
        userRemote.editUser(user)
    }

    override fun saveUserId(userId: String) {
        userPreferences.saveUserId(userId)
    }

    override fun fetchUserId(): String? {
        return userPreferences.fetchUserId()
    }

}