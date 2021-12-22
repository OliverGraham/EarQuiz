package com.projects.oliver_graham.earquizmvp.quizscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.oliver_graham.earquizmvp.R
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.data.Note
import com.projects.oliver_graham.earquizmvp.data.QuestionsRepo
import com.projects.oliver_graham.earquizmvp.data.QuizQuestion
import com.projects.oliver_graham.earquizmvp.navigation.HOME_INDEX
import com.projects.oliver_graham.earquizmvp.navigation.LEADERBOARD_INDEX
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.sounds.SoundPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class QuizScreenViewModel(
    private val navController: NavigationController,
    private val firebaseController: FirebaseController,
    private val isTakingQuiz: MutableState<Boolean>,
    private val navItemSelectedIndex: MutableState<Int>,
    private val soundPlayer: SoundPlayer
) : ViewModel() {

    val quizName = "Intervals"          // get quiz name from nav?
    val totalQuestions = 5              // could pass different amount later
    private val repo = QuestionsRepo
    private val intervalsByHalfStepMap = repo.getIntervalsByHalfStep()
    private val noteList = repo.getNotes()

    val questionNumber: MutableState<Int> = mutableStateOf(value = 1)
    val correctUserAnswers: MutableState<Int> = mutableStateOf(value = 0)
    val incorrectUserAnswers: MutableState<Int> = mutableStateOf(value = 0)
    val numberOfIntervalTaps: MutableState<Int> = mutableStateOf(value = 0)
    val currentUserChoice: MutableState<Int> = mutableStateOf(value = 0)

    val submitButtonEnabled: MutableState<Boolean> = mutableStateOf(value = false)
    val playButtonEnabled: MutableState<Boolean> = mutableStateOf(value = true)
    val nextButtonEnabled: MutableState<Boolean> = mutableStateOf(value = true)
    val showAnswerDialog: MutableState<Boolean> = mutableStateOf(value = false)
    val showFinishedDialog: MutableState<Boolean> = mutableStateOf(value = false)

    val currentCorrectAnswer: MutableState<QuizQuestion> = mutableStateOf(value = QuizQuestion())
    val radioGroup: SnapshotStateList<QuizQuestion> = mutableStateListOf()

    init {
        resetQuizPage()
    }

    fun resetQuizScreen() {
        questionNumber.value = 1
        correctUserAnswers.value = 0
        incorrectUserAnswers.value = 0
        numberOfIntervalTaps.value = 0

        playButtonEnabled.value = true
        nextButtonEnabled.value = true
        showAnswerDialog.value = false
        showFinishedDialog.value = false

        currentCorrectAnswer.value = QuizQuestion(id = 0, text = "", firstNote = 0, secondNote = 0)
        isTakingQuiz.value = false
        resetQuizPage()
    }

    private fun emptyRadioGroup() = radioGroup.removeRange(0, radioGroup.size)

    private fun resetQuizPage() {

        // unselect radio button, disable submit button and empty the four random choices
        currentUserChoice.value = 0
        submitButtonEnabled.value = false
        emptyRadioGroup()

        // three random intervals and one correct
        val twoCurrentNotes: List<Note> = createTwoRandomNotes()
        val noteOne = twoCurrentNotes[0]
        val noteTwo = twoCurrentNotes[1]
        val keyInterval: Int = kotlin.math.abs(n = noteOne.pitch - noteTwo.pitch)
        val randomIntervals: List<Int> = getRandomIntervals(keyInterval)

        soundPlayer.setCurrentSequence(note1 = noteOne.pitch, note2 = noteTwo.pitch)

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

        radioGroup.shuffle()
    }


    fun determineOutcome() = currentCorrectAnswer.value.id == currentUserChoice.value

    fun convertUserChoiceToText() = intervalsByHalfStepMap.getValue(currentUserChoice.value)

    fun playSound(countTowardScore: Boolean = true) = viewModelScope.launch { ->

        playButtonEnabled.value = false

        if (countTowardScore)
            numberOfIntervalTaps.value++

        soundPlayer.playNote1()
        delay(timeMillis = 1000)
        soundPlayer.playNote2()
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
            showFinishedDialog.value = !showFinishedDialog.value
        } else {
            questionNumber.value++
            resetQuizPage()
        }
    }

    fun navToHomeScreen() {
        navItemSelectedIndex.value = HOME_INDEX
        navController.navHomeScreenPopAndTop()
    }

    fun navToLeaderboardScreen() {
        navItemSelectedIndex.value = LEADERBOARD_INDEX
        navController.navLeaderboardScreenPopAndTop()
    }

    // later, could save results of individual quizzes. For now, just save right and wrong answers
    private fun saveResultsToFirestore() {

        if (firebaseController.isUserLoggedIn()) {
            val currentUser = firebaseController.getUserDocument()
            val updatedUser = currentUser?.copy(
                correctAnswers = currentUser.correctAnswers + correctUserAnswers.value,
                incorrectAnswers = currentUser.incorrectAnswers + incorrectUserAnswers.value
            )
            firebaseController.updateUserDocument(updatedUser)
        }
        // TODO: else "no results saved, create account to compare your score with others"
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
            if (kotlin.math.abs(n = randomNoteOne.pitch - randomNoteTwo.pitch) in 1..12)
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

