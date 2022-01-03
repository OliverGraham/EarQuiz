package com.projects.oliver_graham.earquizmvp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry

private const val TRANSITION_DURATION = 750
private const val TRANSITION_OFFSET = 500
private const val NAV_BAR_DURATION = 1000
private const val NAV_ITEM_DURATION = 500

private const val TO_RIGHT = 500
private const val TO_LEFT = -500

private fun getOffsetForEnterTransition(startRoute: String?, endRoute: String?): Int {

    when (startRoute) {
        Screen.LoginScreen.route -> when (endRoute) {
            Screen.CreateAccountScreen.route -> return TO_RIGHT
            Screen.HomeScreen.route -> return TO_RIGHT
        }
        Screen.CreateAccountScreen.route -> when (endRoute) {
            Screen.LoginScreen.route -> return TO_LEFT
            Screen.HomeScreen.route -> return TO_RIGHT
        }
        Screen.HomeScreen.route -> when (endRoute) {
            Screen.QuizScreen.route -> return TO_RIGHT
            Screen.LeaderboardScreen.route -> return TO_RIGHT
        }
        Screen.QuizScreen.route -> when (endRoute) {
            Screen.LeaderboardScreen.route -> return TO_RIGHT
            // quiz to home is handled somewhere else
        }
        Screen.LeaderboardScreen.route -> when (endRoute) {
            Screen.QuizScreen.route -> return TO_LEFT
            // LB to home is handled somewhere else
        }
    }

    return TO_LEFT
}

@ExperimentalAnimationApi
fun enterTransition(
    offset: Int = TRANSITION_OFFSET
): (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> EnterTransition?) =
    { start, end ->
        slideInHorizontally(
            initialOffsetX =
                { getOffsetForEnterTransition(start.destination.route, end.destination.route) },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = FastOutSlowInEasing
            )
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
    }


@ExperimentalAnimationApi
fun popExitTransition(
    offset: Int = TRANSITION_OFFSET
): (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> ExitTransition) =
    { _, _ ->
        slideOutHorizontally(
            targetOffsetX = { offset },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = FastOutSlowInEasing
            )
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
    }

@ExperimentalAnimationApi
fun exitTransition(
    offset: Int = TRANSITION_OFFSET
): (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> ExitTransition) =
    { _, _ ->
        slideOutHorizontally(
            targetOffsetX = { -offset },
            animationSpec = tween(durationMillis = TRANSITION_DURATION, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
    }

@ExperimentalAnimationApi
fun popEnterTransition(
    offset: Int = TRANSITION_OFFSET
): (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> EnterTransition?) =
    { _, _ ->
        slideInHorizontally(
            initialOffsetX = { -offset },
            animationSpec = tween(durationMillis = TRANSITION_DURATION, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
    }


@ExperimentalAnimationApi
fun slideInVertically(): EnterTransition = slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(durationMillis = NAV_BAR_DURATION, delayMillis = NAV_BAR_DURATION)
)

@Composable
fun AnimatableIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    scale: Float = 1f,
    color: Color = MaterialTheme.colors.onPrimary
) {
    // Animation parameters
    val animatedScale: Float by animateFloatAsState(
        targetValue = scale,
        animationSpec = TweenSpec(durationMillis = NAV_ITEM_DURATION, easing = FastOutSlowInEasing)
    )
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = TweenSpec(durationMillis = NAV_ITEM_DURATION, easing = FastOutSlowInEasing)
    )
    Icon(
        imageVector = imageVector,
        contentDescription = "",
        tint = animatedColor,
        modifier = modifier.scale(animatedScale).size(iconSize)
    )
}
