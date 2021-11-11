package com.projects.oliver_graham.earquizmvp.quizpage

import android.app.Application
import android.content.res.Resources
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.projects.oliver_graham.earquizmvp.data.QuestionsRepo
import com.projects.oliver_graham.earquizmvp.data.QuizQuestion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import android.media.MediaPlayer.OnPreparedListener
import android.util.Log
import com.projects.oliver_graham.earquizmvp.data.Note
import java.lang.Math.abs
import kotlin.random.Random


class QuizPageViewModel(application: Application) : AndroidViewModel(application) {

    val quizName = "Intervals"          // get quiz name from nav?
    val totalQuestions = 2              // could pass different amount later
    private val repo = QuestionsRepo
    private val intervalsByHalfStepMap = repo.getIntervalsByHalfStep()
    private val noteList = repo.getNotes()
    private val urlPath = "http://192.168.4.21:8080/download/test2/"

    val questionNumber: MutableState<Int> = mutableStateOf(1)
    val correctUserAnswers: MutableState<Int> = mutableStateOf(0)
    val incorrectUserAnswers: MutableState<Int> = mutableStateOf(0)
    val numberOfIntervalTaps: MutableState<Int> = mutableStateOf(0)
    val currentUserChoice: MutableState<Int> = mutableStateOf(0)
    val submitButtonEnabled: MutableState<Boolean> = mutableStateOf(false)
    val playButtonEnabled: MutableState<Boolean> = mutableStateOf(true)
    val nextButtonEnabled: MutableState<Boolean> = mutableStateOf(true)
    // val showAnswerDialog: MutableState<Boolean> = mutableStateOf(true)

    val currentCorrectAnswer:
          MutableState<QuizQuestion> = mutableStateOf(QuizQuestion(0, "", ""))  // was string

    val radioGroup: SnapshotStateList<QuizQuestion> = mutableStateListOf()

    private var appContext: Application? = null
    var player1: MediaPlayer? = null
    var player2: MediaPlayer? = null

    init {
        viewModelScope.launch {

            player1 = MediaPlayer()
            player2 = MediaPlayer()
            resetQuizPage()
        }
    }

    fun resetQuizPage() {

        player1?.reset()
        player2?.reset()

        // unselect radio button, disable submit button and empty the four random choices
        currentUserChoice.value = 0
        submitButtonEnabled.value = false
        radioGroup.removeRange(0, radioGroup.size)

        // three random intervals and one correct
        val twoCurrentNotes: List<Note> = createTwoRandomNotes()
        val keyInterval: Int = kotlin.math.abs(twoCurrentNotes[0].id - twoCurrentNotes[1].id)
        val randomIntervals: List<Int> = getRandomIntervals(keyInterval)

        currentCorrectAnswer.value = QuizQuestion(
            id = keyInterval,
            text = intervalsByHalfStepMap.getValue(keyInterval)
        )

        randomIntervals.forEach { interval ->
            radioGroup.add(QuizQuestion(
                id = interval,
                text = intervalsByHalfStepMap.getValue(interval)
                )
            )
        }

        setMediaPlayers(
            noteOne = urlPath + twoCurrentNotes[0].soundPath,
            noteTwo = urlPath + twoCurrentNotes[1].soundPath
        )
        radioGroup.shuffle()
    }

    private fun setMediaPlayers(noteOne: String, noteTwo: String) {
        try {
            player1?.setDataSource(noteOne)
            player1?.prepareAsync()

            player2?.setDataSource(noteTwo)
            player2?.prepareAsync()

        } catch (ioException: IOException) {
            Toast.makeText(appContext?.applicationContext, "Unable to connect to sound database." +
                    " please check internet connection.", Toast.LENGTH_SHORT).show()
            // maybe log exception later
        }
    }

    fun determineOutcome() = currentCorrectAnswer.value.id == currentUserChoice.value

    fun convertUserChoiceToText() = intervalsByHalfStepMap.getValue(currentUserChoice.value)

    fun playSound(countTowardScore: Boolean = true) = viewModelScope.launch {

        playButtonEnabled.value = false

        if (countTowardScore)
            numberOfIntervalTaps.value++

        player1?.start()
        delay(timeMillis = 1000)
        player2?.start()
        delay(timeMillis = 1000)

        playButtonEnabled.value = true
    }


    private fun createTwoRandomNotes(): List<Note> {

        val size = noteList.size
        val randomNoteOne = noteList[Random.nextInt(size)]
        val randomNoteTwo = noteList[Random.nextInt(size)]
        val intervalDistance = kotlin.math.abs(randomNoteOne.id - randomNoteTwo.id)

        if (intervalDistance in 1..12)
            return listOf(randomNoteOne, randomNoteTwo)

        return createTwoRandomNotes()
    }

    private fun getRandomIntervals(keyInterval: Int, randomListSize: Int = 4): List<Int> {

        val randomIntervalList = mutableListOf(keyInterval)

        while (randomIntervalList.size < randomListSize) {
            val currentRandom = (1..12).random()

            if (!randomIntervalList.contains(currentRandom))
                randomIntervalList.add(currentRandom)
        }
        return randomIntervalList
    }

}



