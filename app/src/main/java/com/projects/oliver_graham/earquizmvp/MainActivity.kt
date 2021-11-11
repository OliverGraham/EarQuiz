package com.projects.oliver_graham.earquizmvp

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.projects.oliver_graham.earquizmvp.quizpage.QuizPage
import com.projects.oliver_graham.earquizmvp.quizpage.QuizPageViewModel
import com.projects.oliver_graham.earquizmvp.ui.BackGroundImage
import com.projects.oliver_graham.earquizmvp.ui.theme.EarQuizMVPTheme

class MainActivity : ComponentActivity() {

    private val viewModel: QuizPageViewModel by viewModels()

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            EarQuizMVPTheme {

                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor (
                    color = MaterialTheme.colors.primary
                )

                // will go to nav, in nav, viewModel can be given # of questions (later)
                BackGroundImage {

                    Surface(
                        color = MaterialTheme.colors.background.copy(alpha = 0.925f),
                    ) {
                        QuizPage(viewModel = viewModel)
                    }
                }

            }
        }
    }
}


/*Image(
painter = painterResource(id = R.drawable.p5ae),
contentDescription = null
)*/
