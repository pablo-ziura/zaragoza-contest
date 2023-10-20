package com.zaragoza.contest.model

data class User(
    var id: String = "",
    val email: String = "",
    var password: String = "",
    val nickname: String = "",
    val urlImage: String? = null,
    var score: Int? = 0
)