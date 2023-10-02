package com.zaragoza.contest.data.question

import com.zaragoza.contest.domain.QuestionRepository
import com.zaragoza.contest.domain.model.Question

class QuestionRepositoryImpl(private val questionRemote: QuestionRepository) : QuestionRepository {
    override suspend fun getQuestionList(): List<Question> {
        return questionRemote.getQuestionList()
    }

}