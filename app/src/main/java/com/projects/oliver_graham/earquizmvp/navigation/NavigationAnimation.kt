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

@ExperimentalAnimationApi
fun enterTransition(
    offset: Int = TRANSITION_OFFSET
):
        (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> EnterTransition?) =
    { start, end ->

        // TODO: so the logic is... positive == going right, and negative == going left?
        val newOffset = if (end.destination.route == "leaderboard_screen") {
            500
        } else if (end.destination.route == "quiz_screen") {
            if (start.destination.route == "leaderboard_screen") {
                -500
            } else {
                500
            }
        } else {
            -500        // homescreen from loginscreen looks weird
        }

        slideInHorizontally(
            initialOffsetX = { newOffset },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = FastOutSlowInEasing
            )
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
    }


@ExperimentalAnimationApi
fun popExitTransition(
    offset: Int = TRANSITION_OFFSET
):
        (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> ExitTransition) =
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
):
        (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> ExitTransition) =
    { _, _ ->
        slideOutHorizontally(
            targetOffsetX = { -offset },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = FastOutSlowInEasing
            )
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
    }

@ExperimentalAnimationApi
fun popEnterTransition(
    offset: Int = TRANSITION_OFFSET
):
        (AnimatedContentScope<String>.(NavBackStackEntry, NavBackStackEntry) -> EnterTransition?) =
    { _, _ ->
        slideInHorizontally(
            initialOffsetX = { -offset },
            animationSpec = tween(
                durationMillis = TRANSITION_DURATION,
                easing = FastOutSlowInEasing
            )
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
    }


@ExperimentalAnimationApi
fun slideInVertically(): EnterTransition =
    slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(durationMillis = NAV_BAR_DURATION, delayMillis = NAV_BAR_DURATION))



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
        animationSpec = TweenSpec(
            durationMillis = NAV_ITEM_DURATION,
            easing = FastOutSlowInEasing
        )
    )
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = TweenSpec(
            durationMillis = NAV_ITEM_DURATION,
            easing = FastOutSlowInEasing
        )
    )
    Icon(
        imageVector = imageVector,
        contentDescription = "",
        tint = animatedColor,
        modifier = modifier.scale(animatedScale).size(iconSize)
    )
}
