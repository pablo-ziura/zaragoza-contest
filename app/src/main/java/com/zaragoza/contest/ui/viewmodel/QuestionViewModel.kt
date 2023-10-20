package com.zaragoza.contest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaragoza.contest.domain.usecase.question.GetQuestionListUseCase
import com.zaragoza.contest.model.Question
import com.zaragoza.contest.ui.common.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias GetQuestionListState = ResourceState<List<Question>>

class QuestionViewModel(
    private val getQuestionListUseCase: GetQuestionListUseCase
) : ViewModel() {

    private val _getQuestionListLiveData = MutableLiveData<GetQuestionListState>()
    val getQuestionListLiveData: LiveData<GetQuestionListState> get() = _getQuestionListLiveData

    private var currentQuestionIndex = 0

    fun getQuestionList() {
        _getQuestionListLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val notes = getQuestionListUseCase.execute()

                withContext(Dispatchers.Main) {
                    _getQuestionListLiveData.value = ResourceState.Success(notes)
                    _getQuestionListLiveData.value = ResourceState.None()
                }
            } catch (e: Exception) {
                _getQuestionListLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                _getQuestionListLiveData.value = ResourceState.None()
            }
        }

    }

    fun resetGame() {
        currentQuestionIndex = 0
    }

    fun getCurrentQuestionIndex(): Int {
        return currentQuestionIndex
    }

    fun getNextQuestion() {
        currentQuestionIndex++
        getQuestionList()
    }

}