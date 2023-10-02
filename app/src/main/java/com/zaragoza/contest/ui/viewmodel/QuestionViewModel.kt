package com.zaragoza.contest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaragoza.contest.domain.model.Question
import com.zaragoza.contest.domain.usecase.question.GetQuestionListUseCase
import com.zaragoza.contest.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias GetQuestionListState = ResourceState<List<Question>>


class QuestionViewModel(
    private val getQuestionListUseCase: GetQuestionListUseCase
) : ViewModel() {

    private val _getQuestionListLiveData = MutableLiveData<GetQuestionListState>()
    val getQuestionListLiveData: LiveData<GetQuestionListState> get() = _getQuestionListLiveData

    suspend fun getQuestionList() {

        _getQuestionListLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val notes = getQuestionListUseCase.execute()

                withContext(Dispatchers.Main) {
                    _getQuestionListLiveData.value = ResourceState.Success(notes)
                }
            } catch (e: Exception) {
                _getQuestionListLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
            }
        }


    }

}