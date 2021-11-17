package com.projects.oliver_graham.earquizmvp.homescreen

import androidx.lifecycle.ViewModel
import com.projects.oliver_graham.earquizmvp.data.QuestionsRepo
import com.projects.oliver_graham.earquizmvp.data.QuizDescription

class HomeScreenViewModel : ViewModel() {

    private val repo = QuestionsRepo
    val quizDescriptions: List<QuizDescription> = repo.getAllQuizDescriptions()

}