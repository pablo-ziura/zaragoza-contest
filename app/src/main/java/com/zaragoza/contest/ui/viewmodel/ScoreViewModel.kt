package com.zaragoza.contest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaragoza.contest.domain.usecase.score.FetchCurrentScoreUseCase
import com.zaragoza.contest.domain.usecase.score.GetBestScoresUseCase
import com.zaragoza.contest.domain.usecase.score.UpdateCurrentUserScoreUseCase
import com.zaragoza.contest.model.Score
import com.zaragoza.contest.ui.common.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias GetBestScoresListState = ResourceState<List<Score>>

class ScoreViewModel(
    private val updateCurrentUserScoreUseCase: UpdateCurrentUserScoreUseCase,
    private val fetchCurrentScoreUseCase: FetchCurrentScoreUseCase,
    private val getBestScoresUseCase: GetBestScoresUseCase
) : ViewModel() {

    private val _getBestScoresListLiveData = MutableLiveData<GetBestScoresListState>()
    val getBestScoresListLiveData: LiveData<GetBestScoresListState> get() = _getBestScoresListLiveData

    fun updateCurrentUserScore(score: Int): ResourceState<Void?> {
        try {
            updateCurrentUserScoreUseCase.execute(score)
            return ResourceState.Success(null)
        } catch (e: Exception) {
            throw Exception("Error en updateCurrentUserScore: ${e.localizedMessage.orEmpty()}")
        }
    }

    fun fetchCurrentScore(): Int {
        try {
            return fetchCurrentScoreUseCase.execute()
        } catch (e: Exception) {
            throw Exception("Error en fetchCurrentScore: ${e.localizedMessage.orEmpty()}")
        }
    }

    fun getBestScores() {
        _getBestScoresListLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bestScores = getBestScoresUseCase.execute()

                withContext(Dispatchers.Main) {
                    _getBestScoresListLiveData.value = ResourceState.Success(bestScores)
                    _getBestScoresListLiveData.value = ResourceState.None()
                }
            } catch (e: Exception) {
                throw Exception("Error en getBestScores: ${e.localizedMessage.orEmpty()}")
            }
        }
    }
}