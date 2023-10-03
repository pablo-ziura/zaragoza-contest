package com.zaragoza.contest.domain.usecase.question

import com.zaragoza.contest.domain.QuestionRepository
import com.zaragoza.contest.model.Question

class GetQuestionListUseCase(private val questionRepository: QuestionRepository) {

    suspend fun execute(): List<Question> {
        return questionRepository.getQuestionList()
    }

}