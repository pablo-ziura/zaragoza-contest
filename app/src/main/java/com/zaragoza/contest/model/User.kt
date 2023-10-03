package com.zaragoza.contest.model

data class User(
    var id: String = "",
    val email: String = "",
    val password: String = "",
    val nickname: String = "",
    val urlImage: String? = null,
    val score: Int? = 0
)