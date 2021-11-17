package com.projects.oliver_graham.earquizmvp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreen
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreenViewModel

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainNavigation(

) {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(HomeScreenViewModel())
        }
    }

}
