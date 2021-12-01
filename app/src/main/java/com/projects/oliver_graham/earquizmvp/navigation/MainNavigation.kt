package com.projects.oliver_graham.earquizmvp.navigation

import android.content.Context

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
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
import kotlin.math.log
import com.projects.oliver_graham.earquizmvp.navigation.authNavGraph
import kotlinx.coroutines.delay
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainNavigation(
    context: Context
) {
    val navController = rememberAnimatedNavController()
    val navWrapper = remember { NavigationController(navController) }
    val firebaseController = remember { FirebaseController(navWrapper, context) }

    // state that needs to shared across app
    val isTakingQuiz: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    val showBottomNavBar: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    val navItemSelectedIndex: MutableState<Int> = remember { mutableStateOf(0) }    // needs to be changed any time there's navigation sigh

    // FOR TESTING ONLY
    if (firebaseController.isUserLoggedIn()) {
        firebaseController.logOutUserFromFirebase()
        firebaseController.logOutUserFromGoogle()
    }

    val loginScreenViewModel = remember { LoginScreenViewModel(navWrapper, firebaseController) }
    val createAccountScreenViewModel =
        remember { CreateAccountScreenViewModel(navWrapper, firebaseController) }
    val homeScreenViewModel = remember { HomeScreenViewModel(navWrapper, isTakingQuiz, navItemSelectedIndex) }
    val quizScreenViewModel = remember { QuizScreenViewModel(navWrapper, firebaseController, isTakingQuiz, navItemSelectedIndex) }
    val leaderboardScreenViewModel =
        remember { LeaderboardScreenViewModel(navWrapper, firebaseController, navItemSelectedIndex) }

    Scaffold(
        bottomBar = {
                BottomBar(
                    navController = navController,
                    screenList = listOf(
                        Screen.HomeScreen,
                        Screen.QuizScreen,
                        Screen.LeaderboardScreen
                    ),
                    showBottomNavBar = showBottomNavBar.value,
                    isTakingQuiz = isTakingQuiz.value,
                    navItemSelectedIndex = navItemSelectedIndex
                )
        }

    ) { innerPadding ->

        AnimatedNavHost(
            navController = navController,
            startDestination = if (firebaseController.isUserLoggedIn()) HOME_GRAPH_ROUTE else AUTH_GRAPH_ROUTE,
            route = ROOT_GRAPH_ROUTE,
            modifier = Modifier.padding(innerPadding)
            //startDestination = Screen.LeaderboardScreen.route
        ) { ->
            authNavGraph(
                showBottomNavBar = showBottomNavBar,
                loginScreenViewModel = loginScreenViewModel,
                createAccountScreenViewModel = createAccountScreenViewModel
            )
            homeNavGraph(
                showBottomNavBar = showBottomNavBar,
                homeScreenViewModel = homeScreenViewModel,
                quizScreenViewModel = quizScreenViewModel,
                leaderboardScreenViewModel = leaderboardScreenViewModel
            )
        }
    }

}




@Composable
fun TopBar(
    firebaseController: FirebaseController
) {

}


@ExperimentalAnimationApi
@Composable
fun BottomBar(
    navController: NavController,
    screenList: List<Screen>,
    showBottomNavBar: Boolean,
    isTakingQuiz: Boolean,
    navItemSelectedIndex: MutableState<Int>
) {

    AnimatedVisibility(
        visible = showBottomNavBar,
        enter = slideInVertically()
        //exit = slideOutVertically(targetOffsetY = { it })
    ) { ->
        BottomNavigation { ->
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            screenList.forEachIndexed { index, screen ->
                BottomNavigationItem(
                    icon = {
                        AnimatableIcon(
                            imageVector = screen.icon,
                            scale = if (navItemSelectedIndex.value == index) 1.75f else 1.0f,
                            color = if (navItemSelectedIndex.value == index) MaterialTheme.colors.secondary else MaterialTheme.colors.onPrimary
                        )
                           },
                    label = { screen.route },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        if (isTakingQuiz)
                            bottomNavClick(
                                navController = navController,
                                route = screen.route,
                                navItemSelectedIndex = navItemSelectedIndex,
                                index = index
                            )
                         else if (screen.route != Screen.QuizScreen.route)
                            bottomNavClick(
                                navController = navController,
                                route = screen.route,
                                navItemSelectedIndex = navItemSelectedIndex,
                                index = index
                            )
                    }
                )
            }
        }
    }
}


private fun bottomNavClick(
    navController: NavController,
    route: String,
    navItemSelectedIndex: MutableState<Int>,
    index: Int
) {
    navItemSelectedIndex.value = index
    navController.navigate(route) { ->
        popUpTo(Screen.HomeScreen.route) { ->
            //saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
