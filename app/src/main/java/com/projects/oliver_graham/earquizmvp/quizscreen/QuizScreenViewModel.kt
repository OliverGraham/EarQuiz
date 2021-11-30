package com.projects.oliver_graham.earquizmvp.quizscreen



import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.projects.oliver_graham.earquizmvp.R
import com.projects.oliver_graham.earquizmvp.data.*
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.navigation.Screen

import kotlin.random.Random


// class QuizPageViewModel(application: Application) : AndroidViewModel(application) {
class QuizScreenViewModel(
    private val navController: NavigationController,
    private val firebaseController: FirebaseController
    ) : ViewModel() {

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
    val showAnswerDialog: MutableState<Boolean> = mutableStateOf(false)
    val showFinishedDialog: MutableState<Boolean> = mutableStateOf(false)

    val currentCorrectAnswer: MutableState<QuizQuestion> = mutableStateOf(
        QuizQuestion(id = 0, text = "", firstNote = 0, secondNote = 0))

    val radioGroup: SnapshotStateList<QuizQuestion> = mutableStateListOf()

    //private var appContext: Application? = null
    var player1: MediaPlayer? = null
    var player2: MediaPlayer? = null

    init {
        viewModelScope.launch { ->
            player1 = MediaPlayer()
            player2 = MediaPlayer()
            resetQuizPage()
        }
    }

    fun resetQuizScreen() {
        questionNumber.value = 1
        correctUserAnswers.value = 0
        incorrectUserAnswers.value = 0
        numberOfIntervalTaps.value = 0
       // currentUserChoice.value = 0

       // submitButtonEnabled.value = false
        playButtonEnabled.value = true
        nextButtonEnabled.value = true
        showAnswerDialog.value = false
        showFinishedDialog.value = false

        currentCorrectAnswer.value = QuizQuestion(id = 0, text = "", firstNote = 0, secondNote = 0)
        resetQuizPage()
    }

    fun emptyRadioGroup() = radioGroup.removeRange(0, radioGroup.size)

    fun resetQuizPage() {

        player1?.reset()
        player2?.reset()

        // unselect radio button, disable submit button and empty the four random choices
        currentUserChoice.value = 0
        submitButtonEnabled.value = false
        emptyRadioGroup()

        // three random intervals and one correct
        val twoCurrentNotes: List<Note> = createTwoRandomNotes()
        val noteOne = twoCurrentNotes[0]
        val noteTwo = twoCurrentNotes[1]
        val keyInterval: Int = kotlin.math.abs(noteOne.id - noteTwo.id)
        val randomIntervals: List<Int> = getRandomIntervals(keyInterval)

        currentCorrectAnswer.value = QuizQuestion(
            id = keyInterval,
            text = intervalsByHalfStepMap.getValue(keyInterval),
            clefsImage = R.drawable.treble_and_bass_clef,
            firstNote = noteOne.imageId,
            secondNote = noteTwo.imageId
        )

        randomIntervals.forEach { interval ->
            radioGroup.add(QuizQuestion(
                id = interval,
                text = intervalsByHalfStepMap.getValue(interval)
                )
            )
        }

        setMediaPlayers(
            noteOne = urlPath + noteOne.soundPath,
            noteTwo = urlPath + noteTwo.soundPath
        )
        radioGroup.shuffle()
    }


    fun determineOutcome() = currentCorrectAnswer.value.id == currentUserChoice.value

    fun convertUserChoiceToText() = intervalsByHalfStepMap.getValue(currentUserChoice.value)

    fun playSound(countTowardScore: Boolean = true) = viewModelScope.launch { ->

        playButtonEnabled.value = false

        if (countTowardScore)
            numberOfIntervalTaps.value++

        player1?.start()
        delay(timeMillis = 1000)
        player2?.start()
        delay(timeMillis = 1000)

        playButtonEnabled.value = true
    }

    fun answerDialogDismissRequest(outcome: Boolean) {
        showAnswerDialog.value = !showAnswerDialog.value

        if (outcome)
            correctUserAnswers.value++
        else
           incorrectUserAnswers.value++

        // if end of quiz, open next dialog
        if (questionNumber.value == totalQuestions) {
            saveResultsToFirestore()
            // need to reset quizpage() ?
            showFinishedDialog.value = !showFinishedDialog.value
        } else {
            questionNumber.value++
            resetQuizPage()
        }
    }

    fun navToHomeScreen() = navController.navHomeScreenPopAndTop()

    fun navToLeaderboardScreen() = navController.navLeaderboardScreenPopAndTop()

    // TODO: Should be working well? Double check with:
    //       login a user; create a user; google sign in
    // later, could save results of individual quizzes. For now, just save right and wrong answers
    private fun saveResultsToFirestore() {

        val currentUser = firebaseController.getUserDocument()
        val updatedUser = currentUser?.copy(
            correctAnswers = currentUser.correctAnswers + correctUserAnswers.value,
            incorrectAnswers = currentUser.incorrectAnswers + incorrectUserAnswers.value
        )
        firebaseController.updateUserDocument(updatedUser)
    }

    private fun setMediaPlayers(noteOne: String, noteTwo: String) {
        try {
            player1?.setDataSource(noteOne)
            player1?.prepareAsync()

            player2?.setDataSource(noteTwo)
            player2?.prepareAsync()

            // doesn't catch, even with error
        } catch (ioException: IOException) {
            //Toast.makeText(appContext?.applicationContext, "Unable to connect to sound database." +
            //        " please check internet connection.", Toast.LENGTH_SHORT).show()
            // maybe log exception later
        }
    }

    // avoids mixing sharps and flats for the images
    private fun accidentalPreference(accidental1: Int, accidental2: Int): Boolean {

        // natural can mix with either/or (sometimes not ideal - might make better later)
        if (accidental1 == 0 || accidental2 == 0)
            return true

        if (accidental1 == -1 && accidental2 == -1)
            return true

        if (accidental1 == 1 && accidental2 == 1)
            return true

        return false
    }

    private fun createTwoRandomNotes(): List<Note> {

        val size = noteList.size
        val randomNoteOne = noteList[Random.nextInt(size)]
        val randomNoteTwo = noteList[Random.nextInt(size)]

        if (accidentalPreference(randomNoteOne.accidental, randomNoteTwo.accidental))
            if (kotlin.math.abs(randomNoteOne.id - randomNoteTwo.id) in 1..12)
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

