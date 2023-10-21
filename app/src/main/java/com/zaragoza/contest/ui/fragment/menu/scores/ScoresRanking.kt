package com.zaragoza.contest.ui.fragment.menu.scores

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.zaragoza.contest.model.Score
import com.zaragoza.contest.ui.common.ResourceState
import com.zaragoza.contest.ui.fragment.menu.scores.ui.theme.ZaragozaContestTheme
import com.zaragoza.contest.ui.viewmodel.GetBestScoresListState
import com.zaragoza.contest.ui.viewmodel.ScoreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScoresRanking : ComponentActivity() {

    private val scoreViewModel: ScoreViewModel by viewModel()
    private val bestScores: MutableState<List<Score>> = mutableStateOf(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initUI()
    }

    private fun initViewModel() {
        scoreViewModel.getBestScoresListLiveData.observe(this) { state ->
            handleGetBestScoresListState(state)
        }
        scoreViewModel.getBestScores()
    }

    private fun initUI() {
        setContent {
            ZaragozaContestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Ranking(bestScores.value)
                }
            }
        }
    }

    private fun handleGetBestScoresListState(state: GetBestScoresListState) {
        when (state) {
            is ResourceState.Loading -> {
                Log.i("RESPONSE", "CARGANDO")
            }

            is ResourceState.Success -> {
                bestScores.value = state.result
            }

            is ResourceState.Error -> {
                Toast.makeText(applicationContext, state.error, Toast.LENGTH_LONG).show()
            }

            is ResourceState.None -> {
                // Nada por hacer aquí
            }
        }
    }
}

@Composable
fun Ranking(bestScores: List<Score>) {
    LazyColumn {
        items(bestScores.size) { index ->
            val score = bestScores[index]
            Text("Nickname: ${score.userNickname}, Points: ${score.scorePoints}")
        }
    }
}