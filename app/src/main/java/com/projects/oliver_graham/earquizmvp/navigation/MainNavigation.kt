package com.projects.oliver_graham.earquizmvp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.projects.oliver_graham.earquizmvp.authentication.CreateAccountScreen
import com.projects.oliver_graham.earquizmvp.authentication.CreateAccountScreenViewModel
import com.projects.oliver_graham.earquizmvp.authentication.LoginScreen
import com.projects.oliver_graham.earquizmvp.authentication.LoginScreenViewModel
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreen
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreenViewModel
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreen
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreenViewModel
import kotlin.math.log

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainNavigation(

) {

    val navController = rememberAnimatedNavController() // passed in from MainActivity?
    val auth = FirebaseAuth.getInstance()
    val firestore = Firebase.firestore

    // FOR TESTING ONLY
    if (auth.currentUser != null)
        auth.signOut()

    val loginScreenViewModel = remember { LoginScreenViewModel(navController, auth) }
    val createAccountScreenViewModel =
        remember { CreateAccountScreenViewModel(navController, auth, firestore) }
    val homeScreenViewModel = remember { HomeScreenViewModel(navController) }
    val quizScreenViewModel = remember { QuizScreenViewModel(navController, auth, firestore) }
    // leaderBoard

    val animationDuration = 750
    val animationOffset = 500

    AnimatedNavHost(
        navController = navController,
        startDestination = if (auth.currentUser != null) Screen.HomeScreen.route else Screen.LoginScreen.route
    ) {

        composable(
            route = Screen.HomeScreen.route,
            enterTransition = enterTransition(animationOffset, animationDuration),
            popExitTransition = popExitTransition(animationOffset, animationDuration)
        ) { _ ->
            HomeScreen(viewModel = homeScreenViewModel)
        }

        composable(
            route = Screen.QuizScreen.route,
            enterTransition = enterTransition(animationOffset, animationDuration),
            popExitTransition = popExitTransition(animationOffset, animationDuration)
        ) { _ ->
            QuizScreen(viewModel = quizScreenViewModel)
        }

        composable(
            route = Screen.LoginScreen.route,
            enterTransition = enterTransition(animationOffset, animationDuration),
            popExitTransition = popExitTransition(animationOffset, animationDuration)
        ) { _ ->
            LoginScreen(viewModel = loginScreenViewModel)
        }

        composable(
            route = Screen.CreateAccountScreen.route,
            enterTransition = enterTransition(animationOffset, animationDuration),
            popExitTransition = popExitTransition(animationOffset, animationDuration)
        ) { _ ->
            CreateAccountScreen(viewModel = createAccountScreenViewModel)
        }

    }

}


@ExperimentalAnimationApi
fun enterTransition(
    animationOffset: Int,
    animationDuration: Int
): (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> EnterTransition?) =
    { _, _, ->
       slideInHorizontally(
           initialOffsetX = { animationOffset },
           animationSpec = tween(
               durationMillis = animationDuration,
               easing = FastOutSlowInEasing
           )
       ) + fadeIn(animationSpec = tween(animationDuration))
}

@ExperimentalAnimationApi
fun popExitTransition(
    animationOffset: Int,
    animationDuration: Int
): (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> ExitTransition) =
    { _, _ ->
        slideOutHorizontally(
            targetOffsetX = { animationOffset },
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing
        )
    ) + fadeOut(animationSpec = tween(animationDuration))
}

    /*
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
    },*/
