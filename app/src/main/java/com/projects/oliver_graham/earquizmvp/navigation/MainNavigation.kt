package com.projects.oliver_graham.earquizmvp.navigation

import android.content.Context
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
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreen
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreenViewModel
import com.projects.oliver_graham.earquizmvp.leaderboardscreen.LeaderboardScreen
import com.projects.oliver_graham.earquizmvp.leaderboardscreen.LeaderboardScreenViewModel
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreen
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreenViewModel


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainNavigation(
    context: Context
) {
    val navController = rememberAnimatedNavController() // passed in from MainActivity?
    val navWrapper = remember { NavigationController(navController) }
    val firebaseController = remember { FirebaseController(navWrapper, context) }

    // FOR TESTING ONLY
    if (firebaseController.isUserLoggedIn()) {
        firebaseController.logOutUserFromFirebase()
        firebaseController.logOutUserFromGoogle()
    }

    val loginScreenViewModel = remember { LoginScreenViewModel(navWrapper, firebaseController) }
    val createAccountScreenViewModel =
        remember { CreateAccountScreenViewModel(navWrapper, firebaseController) }
    val homeScreenViewModel = remember { HomeScreenViewModel(navWrapper) }
    val quizScreenViewModel = remember { QuizScreenViewModel(navWrapper, firebaseController) }
    val leaderboardScreenViewModel =
        remember { LeaderboardScreenViewModel(navWrapper, firebaseController) }

    val animationDuration = 750
    val animationOffset = 500

    AnimatedNavHost(
        navController = navController,
        startDestination = if (firebaseController.isUserLoggedIn()) Screen.HomeScreen.route else Screen.LoginScreen.route
        //startDestination = Screen.LeaderboardScreen.route
    ) { ->
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

        composable(
            route = Screen.LeaderboardScreen.route,
            enterTransition = enterTransition(animationOffset, animationDuration),
            popExitTransition = popExitTransition(animationOffset, animationDuration)
        ) { _ ->
           LeaderboardScreen(viewModel = leaderboardScreenViewModel)
        }
    }

}


@ExperimentalAnimationApi
fun enterTransition(
    animationOffset: Int,
    animationDuration: Int
): (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> EnterTransition?) =
    { _, _ ->
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

    // TODO: Are these needed?
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
