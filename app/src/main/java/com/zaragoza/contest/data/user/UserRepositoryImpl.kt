package com.zaragoza.contest.data.user

import com.zaragoza.contest.data.user.remote.UserRemoteImpl
import com.zaragoza.contest.domain.UserRepository
import com.zaragoza.contest.domain.model.User

class UserRepositoryImpl(private val userRemote: UserRemoteImpl) : UserRepository {

    override fun createUser(user: User) {
        userRemote.createUser(user)
    }
}
