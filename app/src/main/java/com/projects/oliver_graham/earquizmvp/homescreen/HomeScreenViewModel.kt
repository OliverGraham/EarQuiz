package com.projects.oliver_graham.earquizmvp.homescreen

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.projects.oliver_graham.earquizmvp.data.QuestionsRepo
import com.projects.oliver_graham.earquizmvp.data.QuizDescription
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.navigation.QUIZ_INDEX
import com.projects.oliver_graham.earquizmvp.navigation.Screen
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val navController: NavigationController,
    private val isTakingQuiz: MutableState<Boolean>,
    private val navItemSelectedIndex: MutableState<Int>
    ) : ViewModel() {

    private val repo = QuestionsRepo
    val quizDescriptions: List<QuizDescription> = repo.getAllQuizDescriptions()

    fun onQuizButtonClick() {
        isTakingQuiz.value = true
        navItemSelectedIndex.value = QUIZ_INDEX
        navController.navQuizScreenSingleTop()
    }
}