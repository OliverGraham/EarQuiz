package com.projects.oliver_graham.earquizmvp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreen
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreenViewModel
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreen
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreenViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainNavigation(

) {

    val navController = rememberAnimatedNavController()
    val homeScreenViewModel = remember { HomeScreenViewModel(navController) }
    val quizScreenViewModel = remember { QuizScreenViewModel(navController) }

    val animationDuration = 750
    val animationOffset = 500

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {

        composable(
            route = Screen.HomeScreen.route,
            exitTransition = {_, _ ->
                slideOutHorizontally(
                    targetOffsetX = { -animationOffset },
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(animationDuration))
            },
            popEnterTransition = { initial, _ ->
                slideInHorizontally(
                    initialOffsetX = { -animationOffset },
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(animationDuration))
            },

        ) {
            HomeScreen(viewModel = homeScreenViewModel)
        }

        composable(
            route = Screen.QuizScreen.route,

            enterTransition = { _, _ ->
                slideInHorizontally(
                    initialOffsetX = { animationOffset },
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(animationDuration))
            },
            popExitTransition = { _, target ->
                slideOutHorizontally(
                    targetOffsetX = { animationOffset },
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(animationDuration))
            }
        ) {
            QuizScreen(viewModel = quizScreenViewModel)
        }
    }

}
