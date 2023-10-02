package com.zaragoza.contest.domain

import com.zaragoza.contest.domain.model.Question

interface QuestionRepository {

    suspend fun getQuestionList(): List<Question>


}