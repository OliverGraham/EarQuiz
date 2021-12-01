package com.projects.oliver_graham.earquizmvp.navigation


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.composable
import com.projects.oliver_graham.earquizmvp.authentication.CreateAccountScreen
import com.projects.oliver_graham.earquizmvp.authentication.CreateAccountScreenViewModel
import com.projects.oliver_graham.earquizmvp.authentication.LoginScreen
import com.projects.oliver_graham.earquizmvp.authentication.LoginScreenViewModel

@ExperimentalAnimationApi
fun NavGraphBuilder.authNavGraph(
    showBottomNavBar: MutableState<Boolean>,
    loginScreenViewModel: LoginScreenViewModel,
    createAccountScreenViewModel: CreateAccountScreenViewModel
) {
    navigation(
        startDestination = Screen.LoginScreen.route,
        route = AUTH_GRAPH_ROUTE
    ) { ->
        composable(
            route = Screen.LoginScreen.route,
            enterTransition = enterTransition(),
            popExitTransition = popExitTransition()
        ) { _ ->
            showBottomNavBar.value = false
            LoginScreen(viewModel = loginScreenViewModel)
        }

        composable(
            route = Screen.CreateAccountScreen.route,
            enterTransition = enterTransition(),
            popExitTransition = popExitTransition()
        ) { _ ->
            showBottomNavBar.value = false
            CreateAccountScreen(viewModel = createAccountScreenViewModel)
        }
    }
}