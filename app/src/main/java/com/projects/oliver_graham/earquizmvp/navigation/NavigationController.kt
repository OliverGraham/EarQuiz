package com.projects.oliver_graham.earquizmvp.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController

class NavigationController(
    private val navController: NavController
) {
    val showBottomNavBar: MutableState<Boolean> = mutableStateOf(value = false)
    val selectedItemIndex: MutableState<Int> = mutableStateOf(value = 0)

    fun navLoginScreen() {
        selectedItemIndex.value = Screen.HomeScreen.screenIndex
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
        selectedItemIndex.value = Screen.HomeScreen.screenIndex
        navController.popBackStack()
        navController.navigate(Screen.HomeScreen.route) { ->
            launchSingleTop = true
        }
    }

    fun navLeaderboardScreenPopAndTop() {
        selectedItemIndex.value = Screen.LeaderboardScreen.screenIndex
        navController.popBackStack()
        navController.navigate(Screen.LeaderboardScreen.route) { ->
            launchSingleTop = true
        }
    }

    fun navCreateAccountScreen() = navController.navigate(Screen.CreateAccountScreen.route)

    fun navQuizScreenSingleTop() {
        selectedItemIndex.value = Screen.QuizScreen.screenIndex
        navController.navigate(Screen.QuizScreen.route) { ->
            launchSingleTop = true  // one copy of quiz on backstack
        }
    }
}