package com.zaragoza.contest.domain

import com.zaragoza.contest.model.User

interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun checkUser(userEmail: String, userPassword: String): String?
    suspend fun getUserInfo(userId: String): User?
    fun saveUserId(userId: String)
    fun fetchUserId(): String?
}