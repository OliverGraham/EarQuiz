package com.projects.oliver_graham.earquizmvp.homescreen

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.projects.oliver_graham.earquizmvp.data.quiz.Quiz
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.navigation.QUIZ_NAV_INDEX


class HomeScreenViewModel(
    private val navController: NavigationController,
    private val isTakingQuiz: MutableState<Boolean>,
    private val quizSelectedIndex: MutableState<Int>,
    private val navItemSelectedIndex: MutableState<Int>,
    private val quizController: Quiz.Companion
    ) : ViewModel() {

    //
    //val quizDescriptions: List<QuizDescription> = QuestionsRepo.getAllQuizDescriptions()
    val quizzes: List<Quiz> = quizController.getAllQuizzes()

    /*fun onQuizButtonClick(quizIndex: Int = 0) {
        isTakingQuiz.value = true
        quizSelectedIndex.value = quizIndex
        navItemSelectedIndex.value = QUIZ_NAV_INDEX
        navController.navQuizScreenSingleTop()
    }*/
    fun onQuizButtonClick(quiz: Quiz) {
        // number of questions?
        quizController.setQuizInProgress(quiz)

        navItemSelectedIndex.value = QUIZ_NAV_INDEX
        navController.navQuizScreenSingleTop()
    }
}