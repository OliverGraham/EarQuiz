package com.projects.oliver_graham.earquizmvp.navigation

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DeleteForever
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
import androidx.navigation.NavDestination
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
                     atHomeScreen = navWrapper.showBottomNavBar.value,
                     endQuiz =  { quizController.stopCurrentQuiz() }
                 )
            },
            bottomBar = {
                BottomBar(
                    navController = navController,
                    screenList = Screen.getHomeScreensList(),
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

@Composable
private fun TopBar(
    navigationController: NavigationController,
    firebaseController: FirebaseController,
    atHomeScreen: Boolean,
    endQuiz: () -> Unit
) {
    val expanded = remember { mutableStateOf(value = false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) { ->
        Image(
            painterResource(id = R.drawable.logo5),
            contentDescription = "EarQuiz logo",
        )
        Column { ->
            AccountIcon(
                atHomeScreen = atHomeScreen,
                expanded = expanded
            )
            AccountDropdownMenu(
                navigationController = navigationController,
                firebaseController = firebaseController,
                atHomeScreen = atHomeScreen,
                expanded = expanded,
                endQuiz = endQuiz
            )
        }
    }
}

@Composable
private fun AccountIcon(
    atHomeScreen: Boolean,
    expanded: MutableState<Boolean>
) {
    Icon(
        Icons.Rounded.AccountCircle,
        modifier = Modifier
            .size(40.dp)
            .shadow(elevation = 20.dp, shape = CircleShape)
            .clickable { if (atHomeScreen) expanded.value = true },
        contentDescription = "",
        tint = if (!isSystemInDarkTheme()) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary
    )
}

@Composable
private fun AccountDropdownMenu(
    navigationController: NavigationController,
    firebaseController: FirebaseController,
    atHomeScreen: Boolean,
    expanded: MutableState<Boolean>,
    endQuiz: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier.background(color = MaterialTheme.colors.background),
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) { ->
        if (atHomeScreen) {
            val isLoggedIn = firebaseController.isUserLoggedIn()
            if (isLoggedIn) {
                DropdownMenuLoggedIn(
                    firebaseController = firebaseController,
                    expanded = expanded,
                    endQuiz = endQuiz
                )
                DropdownDivider()
            }
            DropdownMenuLogout(
                navigationController = navigationController,
                firebaseController = firebaseController,
                isLoggedIn = isLoggedIn,
                expanded = expanded,
                endQuiz = endQuiz
            )
            if (isLoggedIn) {
                DropdownDivider()
                DropdownMenuDeleteUser(
                    firebaseController = firebaseController,
                    expanded = expanded
                )
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
private fun DropdownMenuLoggedIn(
    firebaseController: FirebaseController,
    expanded: MutableState<Boolean>,
    endQuiz: () -> Unit
) {
    AccountIconInnerText(text = "Logged in as:")
    LoggedInTextName(firebaseController.getUserDocument()?.userName)
    DropdownDivider()

    AccountIconInnerText(text = "Use anonymously?")
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
}

@Composable
private fun DropdownMenuLogout(
    navigationController: NavigationController,
    firebaseController: FirebaseController,
    isLoggedIn: Boolean,
    expanded: MutableState<Boolean>,
    endQuiz: () -> Unit
) {
    val text = if (isLoggedIn) {
        "Login as other user?"
    } else {
        "Login"
    }
    AccountIconInnerText(text = text)
    DropdownIcon(
        rowClick = { expanded.value = false },
        icon = Icons.Rounded.Login,
        iconClick = {
            expanded.value = false

            if (isLoggedIn) {
                firebaseController.logOutUserFromFirebase()
                firebaseController.logOutUserFromGoogle()
            }

            navigationController.navLoginScreen()
            endQuiz()
        }
    )
}

@Composable
private fun DropdownMenuDeleteUser(
    firebaseController: FirebaseController,
    expanded: MutableState<Boolean>
) {
    AccountIconInnerText(text = "Delete account?")
    DropdownIcon(
        rowClick = { expanded.value = false },
        icon = Icons.Rounded.DeleteForever,
        iconClick = {
            expanded.value = false
            firebaseController.deleteUser()
        }
    )
}


@Composable
private fun DropdownDivider() {
    Divider(modifier = Modifier.padding(1.dp))
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
            contentDescription = "Dropdown menu option",
            tint = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
private fun LoggedInTextName(userName: String?) {
    AccountIconInnerText(
        text = buildAnnotatedString { ->
            withStyle(style = SpanStyle(color = MaterialTheme.colors.onPrimary)
            ) { ->
                if (userName != null)
                    append(userName)
            }
        }.toString(),
        color = MaterialTheme.colors.onPrimary
    )
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

            // override back-button to change behavior in Home-route
            BackButtonHandler(
                navController = navController,
                quizController = quizController,
                showBottomNavBar = showBottomNavBar,
                navItemSelectedIndex = navItemSelectedIndex,
            )

            screenList.forEachIndexed { index, screen ->
                this.BottomNavItem(
                    navController = navController,
                    quizController = quizController,
                    screen = screen,
                    currentDestination = navBackStackEntry?.destination,
                    selectedIndex = navItemSelectedIndex,
                    index = index
                )
            }
        }
    }
}

@Composable
private fun BackButtonHandler(
    navController: NavController,
    quizController: Quiz.Companion,
    showBottomNavBar: Boolean,
    navItemSelectedIndex: MutableState<Int>,
) {
    val activity = (LocalContext.current as? Activity)
    BackHandler(
        enabled = showBottomNavBar,
        onBack = {
            when (navItemSelectedIndex.value) {

                // Stop quiz and exit if back button pressed on HomeScreen
                Screen.HomeScreen.screenIndex -> {
                    quizController.stopCurrentQuiz()
                    activity?.finish()
                }
                else -> {

                    // otherwise go back to HomeScreen
                    navItemSelectedIndex.value = 0
                    navController.popBackStack()
                }
            }
        }
    )
}

@Composable
private fun RowScope.BottomNavItem(
    navController: NavController,
    quizController: Quiz.Companion,
    screen: Screen,
    currentDestination: NavDestination?,
    selectedIndex: MutableState<Int>,
    index: Int
) {
    BottomNavigationItem(
        icon = {
            BottomNavAnimatableIcon(
                icon = screen.icon,
                selectedIndex = selectedIndex.value,
                currentIndex = index
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            val shouldAllowClick =
                quizController.isQuizInProgress() || screen.route != Screen.QuizScreen.route
            if (shouldAllowClick) {
                bottomNavClick(
                    navController = navController,
                    route = screen.route,
                    navItemSelectedIndex = selectedIndex,
                    index = index
                )
            }
        }
    )
}

@Composable
private fun BottomNavAnimatableIcon(
    icon: ImageVector,
    selectedIndex: Int,
    currentIndex: Int
) {
    AnimatableIcon(
        imageVector = icon,
        scale = if (selectedIndex == currentIndex) 1.75f else 1.0f,
        color = if (selectedIndex == currentIndex)
                    MaterialTheme.colors.secondary
                else
                    MaterialTheme.colors.onPrimary
    )
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
