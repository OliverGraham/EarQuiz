package com.projects.oliver_graham.earquizmvp.quizscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.data.musictheory.MusicTheory
import com.projects.oliver_graham.earquizmvp.data.musictheory.Note
import com.projects.oliver_graham.earquizmvp.data.quiz.Quiz
import com.projects.oliver_graham.earquizmvp.data.quiz.QuizQuestion
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.sounds.SoundPlayer
import kotlinx.coroutines.launch

// TODO: Move quiz logic out of view model. Will strive to have it contain
//       only logic pertaining to the UI

class QuizScreenViewModel(
    private val navController: NavigationController,
    private val firebaseController: FirebaseController,
    private val quizController: Quiz.Companion,
    private val musicTheory: MusicTheory,
    private val soundPlayer: SoundPlayer
    ) : ViewModel() {

    val currentQuiz: Quiz
        get() = quizController.getQuizInProgress()

    val questionNumber:         MutableState<Int> = mutableStateOf(value = 1)
    val correctUserAnswers:     MutableState<Int> = mutableStateOf(value = 0)
    val incorrectUserAnswers:   MutableState<Int> = mutableStateOf(value = 0)
    val numberOfIntervalTaps:   MutableState<Int> = mutableStateOf(value = 0)
    val currentUserChoice:      MutableState<Int> = mutableStateOf(value = 0)

    val submitButtonEnabled:    MutableState<Boolean> = mutableStateOf(value = false)
    val playButtonEnabled:      MutableState<Boolean> = mutableStateOf(value = true)
    val nextButtonEnabled:      MutableState<Boolean> = mutableStateOf(value = true)
    val showAnswerDialog:       MutableState<Boolean> = mutableStateOf(value = false)
    val showFinishedDialog:     MutableState<Boolean> = mutableStateOf(value = false)

    val currentCorrectAnswer: MutableState<QuizQuestion> = mutableStateOf(value = QuizQuestion())
    val radioGroup: SnapshotStateList<QuizQuestion> = mutableStateListOf()

    init {
        resetQuizPage()
    }

    private fun emptyRadioGroup() { radioGroup.removeRange(0, radioGroup.size) }

    private fun resetQuizPage() {

        // unselect radio button, disable submit button, empty the four random choices and sound list
        currentUserChoice.value = 0
        submitButtonEnabled.value = false
        emptyRadioGroup()
        soundPlayer.emptyPitchList()

        // three random intervals and one correct
        val twoCurrentNotes: List<Note> = musicTheory.createTwoRandomNotes()
        val noteOne = twoCurrentNotes[0]
        val noteTwo = twoCurrentNotes[1]
        val keyInterval: Int = kotlin.math.abs(n = noteOne.pitch - noteTwo.pitch)
        val randomIntervals: List<Int> = musicTheory.getRandomIntervals(keyInterval)

        soundPlayer.addPitch(noteOne.pitch)
        soundPlayer.addPitch(noteTwo.pitch)

        quizController.setCurrentQuestion(
            id = keyInterval, text = musicTheory.getIntervalLabel(keyInterval),
            firstNote = noteOne, secondNote = noteTwo
        )
        currentCorrectAnswer.value = quizController.getCurrentQuestion()

        randomIntervals.forEach { interval ->
            radioGroup.add(
                QuizQuestion(id = interval, text = musicTheory.getIntervalLabel(interval))
            )
        }

        radioGroup.shuffle()
    }

    // starting values for new quiz question
    fun resetQuizScreen() {
        questionNumber.value = 1
        correctUserAnswers.value = 0
        incorrectUserAnswers.value = 0
        numberOfIntervalTaps.value = 0

        playButtonEnabled.value = true
        nextButtonEnabled.value = true
        showAnswerDialog.value = false
        showFinishedDialog.value = false

        currentCorrectAnswer.value = QuizQuestion()
        quizController.stopCurrentQuiz()
        resetQuizPage()
    }

    fun getQuizName(): String = quizController.getQuizInProgress().title

    fun determineOutcome(): Boolean = currentCorrectAnswer.value.id == currentUserChoice.value

    fun convertUserChoiceToText(): String = musicTheory.getIntervalLabel(currentUserChoice.value)

    fun playSound(countTowardScore: Boolean = true) = viewModelScope.launch { ->

        playButtonEnabled.value = false

        if (countTowardScore)
            numberOfIntervalTaps.value++

        // plays sound based on which quiz has been selected
        soundPlayer.play(quizIndex = currentQuiz.quizIndex)

        playButtonEnabled.value = true
    }

    fun answerDialogDismissRequest(outcome: Boolean) {
        showAnswerDialog.value = !showAnswerDialog.value

        if (outcome)
            correctUserAnswers.value++
        else
            incorrectUserAnswers.value++

        // if end of quiz, open next dialog
        if (questionNumber.value == currentQuiz.totalQuestions) {
            saveResultsToFirestore()
            showFinishedDialog.value = !showFinishedDialog.value
        } else {
            questionNumber.value++
            resetQuizPage()
        }
    }

    fun navToHomeScreen() { navController.navHomeScreenPopAndTop() }
    fun navToLeaderboardScreen() { navController.navLeaderboardScreenPopAndTop() }

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
}

