package com.projects.oliver_graham.earquizmvp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreen
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreenViewModel
import com.projects.oliver_graham.earquizmvp.leaderboardscreen.LeaderboardScreen
import com.projects.oliver_graham.earquizmvp.leaderboardscreen.LeaderboardScreenViewModel
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreen
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreenViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
fun NavGraphBuilder.homeNavGraph(
    showBottomNavBar: MutableState<Boolean>,
    homeScreenViewModel: HomeScreenViewModel,
    quizScreenViewModel: QuizScreenViewModel,
    leaderboardScreenViewModel: LeaderboardScreenViewModel
) {

    navigation(
        startDestination = Screen.HomeScreen.route,
        route = HOME_GRAPH_ROUTE
    ) { ->
        composable(
            route = Screen.HomeScreen.route,
            enterTransition = enterTransition(),
            exitTransition = exitTransition(),
            popEnterTransition = popEnterTransition(),
            popExitTransition = popExitTransition()
        ) { _ ->
            showBottomNavBar.value = true
            HomeScreen(viewModel = homeScreenViewModel)
        }

        composable(
            route = Screen.QuizScreen.route,
            enterTransition = enterTransition(),
            exitTransition = exitTransition(),
            popEnterTransition = popEnterTransition(),
            popExitTransition = popExitTransition()
        ) { _ ->
            showBottomNavBar.value = true
            QuizScreen(viewModel = quizScreenViewModel)
        }

        composable(
            route = Screen.LeaderboardScreen.route,
            enterTransition = enterTransition(),
            exitTransition = exitTransition(offset = -500),      // sigh, to make positive?
            popEnterTransition = popEnterTransition(),
            popExitTransition = popExitTransition()
        ) { _ ->
            showBottomNavBar.value = true
            LeaderboardScreen(viewModel = leaderboardScreenViewModel)
        }
    }
}