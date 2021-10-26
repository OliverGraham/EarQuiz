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
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.projects.oliver_graham.earquizmvp.quizpage.QuizPage
import com.projects.oliver_graham.earquizmvp.quizpage.QuizPageViewModel
import com.projects.oliver_graham.earquizmvp.ui.theme.EarQuizMVPTheme

class MainActivity : ComponentActivity() {

    private val viewModel: QuizPageViewModel by viewModels()

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val soundPool: SoundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        //viewModel.resetQuizPage()


        setContent {
            EarQuizMVPTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    //Greeting("Android")
                    QuizPage(viewModel = viewModel)

                }
            }
        }
    }
}

/*Image(
painter = painterResource(id = R.drawable.p5ae),
contentDescription = null
)*/




@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EarQuizMVPTheme {
        Greeting("Android")
    }
}