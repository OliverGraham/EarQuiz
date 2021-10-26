package com.projects.oliver_graham.earquizmvp.quizpage

import android.app.Application
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.content.res.Resources
import android.content.res.loader.AssetsProvider
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.oliver_graham.earquizmvp.R
import com.projects.oliver_graham.earquizmvp.data.QuestionsRepo
import com.projects.oliver_graham.earquizmvp.data.QuizQuestion
import com.projects.oliver_graham.earquizmvp.data.RadioQuizInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizPageViewModel(application: Application) : AndroidViewModel(application) {

    val quizName = "Intervals"  // how init later??

    val questionNumber: MutableState<Int> = mutableStateOf(1)
    val correctUserAnswers: MutableState<Int> = mutableStateOf(0)
    val incorrectUserAnswers: MutableState<Int> = mutableStateOf(0)
    val numberOfIntervalTaps: MutableState<Int> = mutableStateOf(0)
    val currentUserChoice: MutableState<String> = mutableStateOf("")
    val submitButtonEnabled: MutableState<Boolean> = mutableStateOf(false)
    val playButtonEnabled: MutableState<Boolean> = mutableStateOf(true)

  //  val currentCorrectAnswer: MutableState<QuizQuestion> = mutableStateOf(QuizQuestion("", ""))
    val currentCorrectAnswer:
          MutableState<QuizQuestion> = mutableStateOf(QuizQuestion("", "", ""))

    val radioGroup: SnapshotStateList<QuizQuestion> = mutableStateListOf()
    var appContext: Application? = null
    var player1: MediaPlayer? = null
    var player2: MediaPlayer? = null

    init {
        viewModelScope.launch {
            appContext = application
            resetQuizPage()

            val res: Resources = application.resources

            val note1 = res.getIdentifier("c", "raw", application.packageName)
            val note2 = res.getIdentifier("eb", "raw", application.packageName)
            player1 = MediaPlayer.create(application.applicationContext, note1)   //R.raw.ae
            player2 = MediaPlayer.create(application.applicationContext, note2)
        }
    }

    fun submitAnswer(userChoice: String) {
        val outcome = currentCorrectAnswer.value.id == userChoice
        val text = if (outcome) "Correct!" else "Incorrect."

        Toast.makeText(
            appContext?.applicationContext,
            "$text It was a ${currentCorrectAnswer.value.text}",
            Toast.LENGTH_SHORT
        ).show()

        if (outcome) {
            correctUserAnswers.value++
        } else {
            incorrectUserAnswers.value++
        }
    }

    fun playSound() = viewModelScope.launch {
        numberOfIntervalTaps.value++
        player1?.start()
        delay(1000)
        player2?.start()
    }

    private fun resetQuizPage() {

        player1?.release()
        player2?.release()

        val radioLabelsMap = QuestionsRepo.getRadioLabelsMap()
        val randomIntervals = radioLabelsMap.keys.asSequence().shuffled().take(4).toMutableList()

        // radio buttons will contain 4 random choices
        //val radioGroup = ArrayList<RadioQuizInfo>()
        randomIntervals.forEach { interval ->
            //radioGroup.add(RadioQuizInfo(interval, radioLabelsMap.getValue(interval)))
            radioGroup.add(QuizQuestion(interval, radioLabelsMap.getValue(interval), ""))
        }

        val correctAnswerKey = randomIntervals.random()
        currentCorrectAnswer.value = QuizQuestion(
            id = correctAnswerKey,
            text = radioLabelsMap.getValue(correctAnswerKey),
            "f"
        )

    }

}



