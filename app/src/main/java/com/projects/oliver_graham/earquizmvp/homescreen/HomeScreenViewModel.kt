package com.projects.oliver_graham.earquizmvp.homescreen

import androidx.lifecycle.ViewModel
import com.projects.oliver_graham.earquizmvp.data.quiz.Quiz
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreenViewModel


class HomeScreenViewModel(
    private val navController: NavigationController,
    private val quizController: Quiz.Companion,
    private val resetQuizPage: () -> Unit
    ) : ViewModel() {

    val quizzes: List<Quiz> = quizController.getAllQuizzes()
    fun isQuizInProgress() = quizController.isThereAQuizInProgress()

    fun onQuizButtonClick(quiz: Quiz, numberOfQuestions: Int) {
        quizController.setQuizInProgress(quiz, numberOfQuestions)
        resetQuizPage()
        navController.navQuizScreenSingleTop()
    }

    fun navigateToQuizScreen() {
        navController.navQuizScreenSingleTop()
    }

}