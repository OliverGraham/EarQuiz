package com.projects.oliver_graham.earquizmvp.homescreen

import androidx.lifecycle.ViewModel
import com.projects.oliver_graham.earquizmvp.data.quiz.Quiz
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController


class HomeScreenViewModel(
    private val navController: NavigationController,
    private val quizController: Quiz.Companion
    ) : ViewModel() {

    val quizzes: List<Quiz> = quizController.getAllQuizzes()
    fun isQuizInProgress() = quizController.isThereAQuizInProgress()

    fun onQuizButtonClick(quiz: Quiz) {
        // TODO: number of questions?
        quizController.setQuizInProgress(quiz)
        navController.navQuizScreenSingleTop()
    }
}