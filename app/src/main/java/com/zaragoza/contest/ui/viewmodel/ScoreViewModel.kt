package com.zaragoza.contest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaragoza.contest.domain.usecase.score.FetchCurrentScoreUseCase
import com.zaragoza.contest.domain.usecase.score.GetBestScoresUseCase
import com.zaragoza.contest.domain.usecase.score.UpdateCurrentUserScoreUseCase
import com.zaragoza.contest.model.Score
import com.zaragoza.contest.ui.common.ResourceState

typealias GetBestScoresListState = ResourceState<List<Score>>

class ScoreViewModel(
    private val updateCurrentUserScoreUseCase: UpdateCurrentUserScoreUseCase,
    private val fetchCurrentScoreUseCase: FetchCurrentScoreUseCase,
    private val getBestScoresUseCase: GetBestScoresUseCase
) : ViewModel() {

    private val _getBestScoresListLiveData = MutableLiveData<GetBestScoresListState>()
    val getBestScoresListLiveData: LiveData<GetBestScoresListState> get() = _getBestScoresListLiveData

    fun getBestScores() {

    }

    fun updateCurrentUserScore(score: Int): ResourceState<Void?> {
        return try {
            updateCurrentUserScoreUseCase.execute(score)
            ResourceState.Success(null)
        } catch (e: Exception) {
            ResourceState.Error(e.localizedMessage.orEmpty())
        }
    }

    fun fetchCurrentScore(): Int {
        return try {
            fetchCurrentScoreUseCase.execute()
        } catch (e: Exception) {
            0
        }
    }

}