package com.projects.oliver_graham.earquizmvp.navigation

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import com.projects.oliver_graham.earquizmvp.data.musictheory.MusicTheory
import com.projects.oliver_graham.earquizmvp.data.quiz.Quiz
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreenViewModel
import com.projects.oliver_graham.earquizmvp.leaderboardscreen.LeaderboardScreenViewModel
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreenViewModel
import com.projects.oliver_graham.earquizmvp.sounds.SoundPlayer
import com.projects.oliver_graham.earquizmvp.ui.BackGroundImage

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainNavigation(
    context: Context,
    soundPlayer: SoundPlayer
) {
    val navController = rememberAnimatedNavController()
    val navWrapper = remember { NavigationController(navController) }
    val firebaseController = remember { FirebaseController(navWrapper, context) }
    val quizController = remember { Quiz }
    val musicTheory = remember { MusicTheory }

    BackGroundImage {
        Scaffold(
            topBar = {
                 TopBar(
                     navigationController = navWrapper,
                     firebaseController = firebaseController,
                     atLoginScreen = navWrapper.showBottomNavBar.value,
                     endQuiz =  { quizController.stopCurrentQuiz() }
                 )
            },
            bottomBar = {
                BottomBar(
                    navController = navController,
                    screenList = listOf(
                        Screen.HomeScreen,
                        Screen.QuizScreen,
                        Screen.LeaderboardScreen
                    ),
                    showBottomNavBar = navWrapper.showBottomNavBar.value,
                    quizController = quizController,
                    navItemSelectedIndex = navWrapper.selectedItemIndex
                )
            },
            backgroundColor = MaterialTheme.colors.background.copy(alpha = 0.925f),

        ) { innerPadding ->

            // TODO: create view model factories

            // all ViewModels instantiated
            val loginScreenViewModel = remember {
                LoginScreenViewModel(navWrapper, firebaseController)
            }
            val createAccountScreenViewModel = remember {
                CreateAccountScreenViewModel(navWrapper, firebaseController)
            }

            val quizScreenViewModel = remember {
                QuizScreenViewModel(navWrapper, firebaseController, quizController, musicTheory, soundPlayer)
            }

            val homeScreenViewModel = remember {
                HomeScreenViewModel(navWrapper, quizController) { quizScreenViewModel.resetQuizPage() }
            }

            val leaderboardScreenViewModel = remember {
                LeaderboardScreenViewModel(navWrapper, firebaseController)
            }

            /*val activity = (LocalContext.current as? Activity)

            BackHandler(
                enabled = navWrapper.showBottomNavBar.value,
                onBack = {
                    if (navWrapper.showBottomNavBar.value) {
                        quizController.stopCurrentQuiz()
                        activity?.finish()
                    } else {
                        navController.popBackStack()
                    }

                }
            )*/

            AnimatedNavHost(
                navController = navController,
                startDestination = if (firebaseController.isUserLoggedIn()) HOME_GRAPH_ROUTE else AUTH_GRAPH_ROUTE,
                route = ROOT_GRAPH_ROUTE,
                modifier = Modifier.padding(innerPadding)
            ) { ->
                authNavGraph(
                    showBottomNavBar = navWrapper.showBottomNavBar,
                    loginScreenViewModel = loginScreenViewModel,
                    createAccountScreenViewModel = createAccountScreenViewModel
                )
                homeNavGraph(
                    showBottomNavBar = navWrapper.showBottomNavBar,
                    homeScreenViewModel = homeScreenViewModel,
                    quizScreenViewModel = quizScreenViewModel,
                    leaderboardScreenViewModel = leaderboardScreenViewModel
                )
            }
        }
    }
}

private fun onBackButtonPressed(
    activity: Activity?,
    quizController: Quiz.Companion,
    selectedItemIndex: MutableState<Int>
) {

}


@Composable
private fun TopBar(
    navigationController: NavigationController,
    firebaseController: FirebaseController,
    atLoginScreen: Boolean,
    endQuiz: () -> Unit
) {
    val expanded = remember { mutableStateOf(value = false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) { ->
        Image(
            painterResource(id = R.drawable.earquiz_header_logo),
            contentDescription = "",
            modifier = Modifier.padding(4.dp)
        )
        Column { ->
            Icon(
                Icons.Rounded.AccountCircle,
                modifier = Modifier
                    .size(40.dp)
                    .shadow(elevation = 20.dp, shape = CircleShape)
                    .clickable { if (atLoginScreen) expanded.value = true },
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
            DropdownMenu(
                modifier = Modifier.background(color = MaterialTheme.colors.background),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) { ->
                if (atLoginScreen) {

                    if (firebaseController.isUserLoggedIn()) {

                        AccountIconInnerText(text = "Logged in as:")
                        LoggedInTextName(firebaseController.getUserDocument()?.userName)
                        Divider(modifier = Modifier.padding(1.dp))

                        DropdownIcon(
                            rowClick = { expanded.value = false },
                            icon = Icons.Rounded.Logout,
                            iconClick = {
                                expanded.value = false
                                firebaseController.logOutUserFromFirebase()
                                firebaseController.logOutUserFromGoogle()
                                endQuiz()
                            }
                        )
                        Divider(modifier = Modifier.padding(1.dp))
                    }
                    DropdownIcon(
                        rowClick = { expanded.value = false },
                        icon = Icons.Rounded.Login,
                        iconClick = {
                            expanded.value = false
                            firebaseController.logOutUserFromFirebase()
                            firebaseController.logOutUserFromGoogle()
                            navigationController.navLoginScreen()
                            endQuiz()
                        }
                    )

                }
            }
        }

    }
}

@Composable
private fun AccountIconInnerText(
    text: String,
    color: Color = Color.Unspecified
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        text = text,
        color = color,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun DropdownIcon(
    rowClick: () -> Unit,
    icon: ImageVector,
    iconClick: () -> Unit
) {
    DropdownMenuItem(
        onClick = { rowClick() }
    ) { ->
        Icon(
            icon,
            modifier = Modifier
                .size(36.dp)
                .weight(weight = 1f)
                .clickable { iconClick() },
            contentDescription = "",
            tint = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
private fun LoggedInTextName(userName: String?) {
    val fancyUserName = buildAnnotatedString { ->
        withStyle(style = SpanStyle(color = MaterialTheme.colors.onPrimary)
        ) { ->
            if (userName != null)
                append(userName)
        }
    }
    AccountIconInnerText(text = fancyUserName.toString(), color = MaterialTheme.colors.onPrimary)
}



@ExperimentalAnimationApi
@Composable
private fun BottomBar(
    navController: NavController,
    screenList: List<Screen>,
    showBottomNavBar: Boolean,
    quizController: Quiz.Companion,
    navItemSelectedIndex: MutableState<Int>
) {

    AnimatedVisibility(
        visible = showBottomNavBar,
        enter = slideInVertically()
    ) { ->
        BottomNavigation { ->
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // override back-button
            val activity = (LocalContext.current as? Activity)
            BackHandler(
                enabled = showBottomNavBar,
                onBack = {
                    when (navItemSelectedIndex.value) {
                        Screen.HomeScreen.screenIndex -> {
                            quizController.stopCurrentQuiz()
                            activity?.finish()
                        }
                        else -> {
                            navItemSelectedIndex.value = 0
                            navController.popBackStack()
                        }
                    }
                }
            )

            screenList.forEachIndexed { index, screen ->
                BottomNavigationItem(
                    icon = {
                        AnimatableIcon(
                            imageVector = screen.icon,
                            scale = if (navItemSelectedIndex.value == index) 1.75f else 1.0f,
                            color = if (navItemSelectedIndex.value == index) MaterialTheme.colors.secondary else MaterialTheme.colors.onPrimary
                        )
                           },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        if (quizController.quizInProgress.value.isInProgress)
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
