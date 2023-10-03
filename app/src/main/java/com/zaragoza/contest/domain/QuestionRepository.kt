package com.zaragoza.contest.domain

import com.zaragoza.contest.model.Question

interface QuestionRepository {

    suspend fun getQuestionList(): List<Question>


}