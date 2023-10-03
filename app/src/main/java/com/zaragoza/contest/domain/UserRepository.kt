package com.zaragoza.contest.domain

import com.zaragoza.contest.model.User


interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun checkUser(userEmail: String, userPassword: String)
    suspend fun getUserInfo(userId: String): User?
}