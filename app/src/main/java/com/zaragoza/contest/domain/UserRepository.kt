package com.zaragoza.contest.domain

import com.zaragoza.contest.model.User

interface UserRepository {
    fun createUser(user: User)
}