package com.zaragoza.contest.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zaragoza.contest.domain.usecase.score.FetchCurrentScoreUseCase
import com.zaragoza.contest.domain.usecase.score.UpdateCurrentUserScoreUseCase
import com.zaragoza.contest.ui.common.ResourceState

class ScoreViewModel(
    private val updateCurrentUserScoreUseCase: UpdateCurrentUserScoreUseCase,
    private val fetchCurrentScoreUseCase: FetchCurrentScoreUseCase
) : ViewModel() {

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