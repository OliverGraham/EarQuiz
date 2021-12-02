package com.projects.oliver_graham.earquizmvp.navigation

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.projects.oliver_graham.earquizmvp.R
import com.projects.oliver_graham.earquizmvp.authentication.CreateAccountScreenViewModel
import com.projects.oliver_graham.earquizmvp.authentication.LoginScreenViewModel
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreenViewModel
import com.projects.oliver_graham.earquizmvp.leaderboardscreen.LeaderboardScreenViewModel
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreenViewModel
import com.projects.oliver_graham.earquizmvp.ui.BackGroundImage

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

    // tracks nav bar button presses, to keep animation in-sync
    val navItemSelectedIndex: MutableState<Int> = remember { mutableStateOf(0) }

    // FOR TESTING ONLY
    /*if (firebaseController.isUserLoggedIn()) {
        firebaseController.logOutUserFromFirebase()
        firebaseController.logOutUserFromGoogle()
    }*/

    val loginScreenViewModel = remember { LoginScreenViewModel(navWrapper, firebaseController) }
    val createAccountScreenViewModel =
        remember { CreateAccountScreenViewModel(navWrapper, firebaseController) }
    val homeScreenViewModel = remember { HomeScreenViewModel(navWrapper, isTakingQuiz, navItemSelectedIndex) }
    val quizScreenViewModel = remember { QuizScreenViewModel(navWrapper, firebaseController, isTakingQuiz, navItemSelectedIndex) }
    val leaderboardScreenViewModel =
        remember { LeaderboardScreenViewModel(navWrapper, firebaseController, navItemSelectedIndex) }

    BackGroundImage {
        Scaffold(
            topBar = {
                     TopBar(firebaseController = firebaseController)
            },
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
            },
            backgroundColor = MaterialTheme.colors.background.copy(alpha = 0.925f),

        ) { innerPadding ->

            AnimatedNavHost(
                navController = navController,
                startDestination = if (firebaseController.isUserLoggedIn()) HOME_GRAPH_ROUTE else AUTH_GRAPH_ROUTE,
                route = ROOT_GRAPH_ROUTE,
                modifier = Modifier.padding(innerPadding)
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
}


@Composable
fun TopBar(
    firebaseController: FirebaseController  // will need later
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) { ->
        Image(
            painterResource(id = R.drawable.eq_onesixnine),
            contentDescription = "",
            modifier = Modifier.padding(4.dp)
        )
        Column { ->
            Icon(
                Icons.Rounded.AccountCircle,
                modifier = Modifier.size(40.dp),
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
        }
    }
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
        popUpTo(Screen.HomeScreen.route)
        launchSingleTop = true
        restoreState = true
    }
}
