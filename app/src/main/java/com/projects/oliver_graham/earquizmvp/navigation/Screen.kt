package com.projects.oliver_graham.earquizmvp.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.projects.oliver_graham.earquizmvp.R

const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"
const val HOME_GRAPH_ROUTE = "home"
const val HOME_INDEX = 0
const val QUIZ_INDEX = 1
const val LEADERBOARD_INDEX = 2


// like enum
sealed class Screen(
    val route: String,
    val icon: ImageVector = Icons.Rounded.Home,
    val screenIndex: Int = -1   // maybe needed when handling back button pressed, later
    ) {
    object HomeScreen : Screen(route = "home_screen", icon = Icons.Rounded.Home, screenIndex = HOME_INDEX)
    object QuizScreen : Screen(route = "quiz_screen", icon = Icons.Rounded.Quiz, screenIndex = QUIZ_INDEX)
    object LoginScreen : Screen(route = "login_screen", icon = Icons.Rounded.Login)
    object CreateAccountScreen : Screen(route ="create_account_screen", icon = Icons.Rounded.Calculate)
    object LeaderboardScreen : Screen(route ="leaderboard_screen", icon = Icons.Rounded.Score, screenIndex = LEADERBOARD_INDEX)
}
