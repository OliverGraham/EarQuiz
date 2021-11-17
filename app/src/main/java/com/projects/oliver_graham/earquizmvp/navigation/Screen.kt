package com.projects.oliver_graham.earquizmvp.navigation

// like enum
sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object QuizScreen : Screen("quiz_screen")
}
