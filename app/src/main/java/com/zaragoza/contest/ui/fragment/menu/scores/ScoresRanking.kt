package com.zaragoza.contest.ui.fragment.menu.scores

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.zaragoza.contest.R
import com.zaragoza.contest.model.Score
import com.zaragoza.contest.ui.common.ResourceState
import com.zaragoza.contest.ui.fragment.menu.scores.ui.theme.ZaragozaContestTheme
import com.zaragoza.contest.ui.viewmodel.GetBestScoresListState
import com.zaragoza.contest.ui.viewmodel.ScoreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScoresRanking : ComponentActivity() {

    private val scoreViewModel: ScoreViewModel by viewModel()
    private val bestScores: MutableState<List<Score>> = mutableStateOf(emptyList())
    private val isLoading = mutableStateOf(false)

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
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .paint(
                                painter = painterResource(R.drawable.background_1),
                                contentScale = ContentScale.FillWidth
                            )
                    ) {
                        if (isLoading.value) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Top
                            ) {
                                Text(
                                    text = "Ranking de Jugadores",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.displayMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Ranking(bestScores.value)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleGetBestScoresListState(state: GetBestScoresListState) {
        when (state) {
            is ResourceState.Loading -> {
                isLoading.value = true
            }

            is ResourceState.Success -> {
                isLoading.value = false
                bestScores.value = state.result
            }

            is ResourceState.Error -> {
                isLoading.value = false
                Toast.makeText(applicationContext, state.error, Toast.LENGTH_LONG).show()
            }

            is ResourceState.None -> {
                isLoading.value = false
            }
        }
    }
}

@Composable
fun Ranking(bestScores: List<Score>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(bestScores.size) { index ->
            val score = bestScores[index]
            UserScoreCard(score)
        }
    }
}

@Composable
fun UserScoreCard(score: Score) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = score.urlImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = score.userNickname.uppercase(),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "${score.scorePoints} puntos",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}