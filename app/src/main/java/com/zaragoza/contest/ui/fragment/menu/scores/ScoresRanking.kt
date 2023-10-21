package com.zaragoza.contest.ui.fragment.menu.scores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zaragoza.contest.ui.fragment.menu.scores.previews.MyPreview
import com.zaragoza.contest.ui.fragment.menu.scores.ui.theme.ZaragozaContestTheme
import com.zaragoza.contest.ui.viewmodel.ScoreViewModel

class ScoresRanking : ComponentActivity() {

    private val scoresViewModel: ScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZaragozaContestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@MyPreview
@Composable
fun Greeting(
    modifier: Modifier = Modifier
) {
    Column {

        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Blue)
                .clickable { }
        )

        Text(
            text = "Hello World",
            modifier = modifier
        )
    }
}