package com.projects.oliver_graham.earquizmvp.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.projects.oliver_graham.earquizmvp.data.QuestionsRepo
import com.projects.oliver_graham.earquizmvp.data.QuizDescription
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.navigation.Screen
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val navController: NavigationController) : ViewModel() {

    private val repo = QuestionsRepo
    val quizDescriptions: List<QuizDescription> = repo.getAllQuizDescriptions()

    fun onQuizButtonClick() {
        navController.navQuizScreenSingleTop()
    }
}