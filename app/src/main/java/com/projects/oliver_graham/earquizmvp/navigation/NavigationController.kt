package com.projects.oliver_graham.earquizmvp.navigation

import androidx.navigation.NavController

class NavigationController(
    private val navController: NavController
) {

    fun navLoginScreen() {
        navController.popBackStack()
        navController.navigate(Screen.LoginScreen.route)
    }

    fun navHomeScreenNoBack() {
        navController.navigate(Screen.HomeScreen.route) { ->
            // so back button doesn't go back to login flow
            popUpTo(Screen.LoginScreen.route) { inclusive = true }
        }
    }

    fun navHomeScreenPopBackstack() {
        navController.popBackStack()
        navController.navigate(Screen.HomeScreen.route)
    }

    fun navHomeScreenPopAndTop() {
        navController.popBackStack()
        navController.navigate(Screen.HomeScreen.route) { ->
            launchSingleTop = true
        }
    }

    fun navLeaderboardScreenPopAndTop() {
        navController.popBackStack()
        navController.navigate(Screen.LeaderboardScreen.route) { ->
            launchSingleTop = true
        }
    }

    fun navCreateAccountScreen() = navController.navigate(Screen.CreateAccountScreen.route)

    fun navQuizScreenSingleTop() {
        navController.navigate(Screen.QuizScreen.route) { ->
            launchSingleTop = true  // one copy of quiz on backstack
        }
    }
}