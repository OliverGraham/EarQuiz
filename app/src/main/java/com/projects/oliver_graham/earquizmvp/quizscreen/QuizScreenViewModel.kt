package com.projects.oliver_graham.earquizmvp.quizscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.data.musictheory.MusicTheory
import com.projects.oliver_graham.earquizmvp.data.quiz.Quiz
import com.projects.oliver_graham.earquizmvp.data.quiz.QuizQuestion
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.sounds.SoundPlayer
import kotlinx.coroutines.launch
import kotlin.random.Random

// TODO: Move quiz logic out of view model. Will strive to have it contain
//       only logic pertaining to the UI

class QuizScreenViewModel(
    private val navController: NavigationController,
    private val firebaseController: FirebaseController,
    private val quizController: Quiz.Companion,
    private val musicTheory: MusicTheory,
    private val soundPlayer: SoundPlayer
    ) : ViewModel() {

    /** */
    val currentQuiz: Quiz
        get() = quizController.getQuizInProgress()
    val currentQuestion: QuizQuestion
        get() = quizController.getCurrentQuestion()

    /** */
    // controls state that is displayed
    val questionNumber:         MutableState<Int> = mutableStateOf(value = 1)
    val correctUserAnswers:     MutableState<Int> = mutableStateOf(value = 0)
    val incorrectUserAnswers:   MutableState<Int> = mutableStateOf(value = 0)
    val numberOfIntervalTaps:   MutableState<Int> = mutableStateOf(value = 0)

    /** */
    // this changes when user presses the (currently radio only) button and this
    // value will be checked against the correct answer
    val currentUserChoice:      MutableState<Int> = mutableStateOf(value = 0)

    /** */
    // controls state that determines if buttons should be enabled or if
    // dialog boxes should be shown
    val submitButtonEnabled:    MutableState<Boolean> = mutableStateOf(value = false)
    val playButtonEnabled:      MutableState<Boolean> = mutableStateOf(value = true)
    val nextButtonEnabled:      MutableState<Boolean> = mutableStateOf(value = true)
    val showAnswerDialog:       MutableState<Boolean> = mutableStateOf(value = false)
    val showFinishedDialog:     MutableState<Boolean> = mutableStateOf(value = false)

    // is needed?
    private val currentCorrectAnswer: MutableState<Int> = mutableStateOf(value = 0)
    private val currentRandomInterval: MutableState<Boolean> = mutableStateOf(value = true)

    val radioGroup: SnapshotStateList<Pair<Int, String>> = mutableStateListOf()

    init {
        // TODO: is this needed then?
        resetQuizPage()
    }

    /** */
    private fun emptyRadioGroup() { radioGroup.removeRange(0, radioGroup.size) }

    /** */
    fun resetQuizPage() {
        resetQuizPartOne()

        if (!currentQuiz.isInProgress) return
        resetIfQuizInProgress()
    }

    /** starting values for new quiz question */
    fun resetQuizScreen() {
        questionNumber.value = 1
        correctUserAnswers.value = 0
        incorrectUserAnswers.value = 0
        numberOfIntervalTaps.value = 0

        playButtonEnabled.value = true
        nextButtonEnabled.value = true
        showAnswerDialog.value = false
        showFinishedDialog.value = false

        // TODO: double check that question doesn't need to be set here
        resetQuizPage()
    }

    /** The quiz-screen title has a newline character in it */
    fun getQuizName(): String =  quizController.getQuizInProgress().quizScreenTitle

    /** */
    fun determineOutcome(): Boolean = currentCorrectAnswer.value == currentUserChoice.value

    /** */
    fun convertUserChoiceToText(): String {

        return musicTheory.getIntervalLabel(currentUserChoice.value)
    }

    /** */
    fun playSound(countTowardScore: Boolean = true) = viewModelScope.launch { ->

        playButtonEnabled.value = false

        if (countTowardScore)
            numberOfIntervalTaps.value++

        soundPlayer.play(
            quiz = currentQuiz,
            randomInterval = currentRandomInterval.value
        )

        playButtonEnabled.value = true
    }

    /** */
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
            quizController.stopCurrentQuiz()
            // todo: stop bottomNavBar
        } else {
            questionNumber.value++
            quizController.removeAskedQuestion()
            currentCorrectAnswer.value = currentQuestion.correctId
            resetQuizPage()
        }
    }

    /** */
    fun navToHomeScreen() { navController.navHomeScreenPopAndTop() }

    /** */
    fun navToLeaderboardScreen() { navController.navLeaderboardScreenPopAndTop() }

    /** Unselect radio button, disable submit button, empty the four random choices and sound list */
    private fun resetQuizPartOne() {
        currentUserChoice.value = 0
        submitButtonEnabled.value = false
        emptyRadioGroup()
        soundPlayer.emptyPitchList()
    }

    private fun resetIfQuizInProgress() {
        // add new sounds and set correct value
        soundPlayer.addPitches(currentQuestion.soundIds)
        currentCorrectAnswer.value = currentQuestion.correctId
        currentRandomInterval.value = Random.nextBoolean()

        // get labels, one correct and three incorrect and shuffle answers
        quizController.getCurrentQuestionLabels()
            .forEach { labelPair -> radioGroup.add(labelPair) }
        radioGroup.shuffle()
    }

    /** */
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

