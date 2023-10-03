package com.zaragoza.contest.model

data class Question(
    var id: Int = -1,
    val statement: String = "",
    val firstAnswer: String = "",
    val secondAnswer: String = "",
    val thirdAnswer: String = "",
    val fourthAnswer: String = "",
    val rightAnswer: Int = -1,
    val isSolved: Int = -1
)