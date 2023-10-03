package com.zaragoza.contest.data.question

import com.zaragoza.contest.data.question.remote.QuestionRemoteImpl
import com.zaragoza.contest.domain.QuestionRepository
import com.zaragoza.contest.model.Question

class QuestionDataRepository(private val questionRemote: QuestionRemoteImpl) : QuestionRepository {
    override suspend fun getQuestionList(): List<Question> {
        return questionRemote.getQuestionList()
    }

}