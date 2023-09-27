package com.zaragoza.contest.domain

import com.zaragoza.contest.domain.model.User


interface UserRepository {
    fun createUser(user: User)
    fun checkUser(userEmail: String, userPassword: String)
    fun getUserInfo(userId: String): User
}