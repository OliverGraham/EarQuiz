package com.projects.oliver_graham.earquizmvp.homescreen

import androidx.lifecycle.ViewModel
import com.projects.oliver_graham.earquizmvp.data.quiz.Quiz
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreenViewModel


class HomeScreenViewModel(
    private val navController: NavigationController,
    private val quizController: Quiz.Companion,
    private val quizScreenViewModel: QuizScreenViewModel //TEMPORARY TEST
    ) : ViewModel() {

    val quizzes: List<Quiz> = quizController.getAllQuizzes()
    fun isQuizInProgress() = quizController.isThereAQuizInProgress()

    fun onQuizButtonClick(quiz: Quiz) {
        // TODO: will get this from number picker or something, here on HomeScreen
        val numberOfQuestions = 3

        // TODO: combine this initialization logic?
        quizController.setQuizInProgress(quiz)
        quizController.createQuizQuestions(numberOfQuestions)

        // should the VM be initialized here???
        quizScreenViewModel.resetQuizPage()

        navController.navQuizScreenSingleTop()

    }
}