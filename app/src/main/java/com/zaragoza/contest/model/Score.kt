package com.zaragoza.contest.model

import com.google.firebase.database.PropertyName

data class Score(
    @PropertyName("nickname") val userNickname: String = "",
    @PropertyName("score") val scorePoints: Int = -1,
    @PropertyName("urlImage") val urlImage: String? = null
)