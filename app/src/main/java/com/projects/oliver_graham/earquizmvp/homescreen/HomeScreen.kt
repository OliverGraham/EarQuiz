package com.projects.oliver_graham.earquizmvp.homescreen

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.projects.oliver_graham.earquizmvp.ui.CenteredContentRow
import com.projects.oliver_graham.earquizmvp.ui.LargeText
import com.projects.oliver_graham.earquizmvp.ui.MediumButton

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState
    ){
        // so header doesn't scroll
        stickyHeader {
            Surface(modifier = Modifier.fillParentMaxWidth()) {
                HeaderRow(
                    headerTitle = "EarQuiz",
                    logo = 0,
                    otherLogo = 0
                )
            }
        }
        // make an expandable quiz description, with button, for every description in the repo
        items(viewModel.quizDescriptions) { quizDescription ->
            Spacer(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ExpandableRow(                   // change name to ExpandableWideCard?
                    title = quizDescription.title,
                    quizDescriptions = quizDescription.descriptions

                )
                MediumButton(onClick = {}) {     // but as SmallButton
                    Text("GO")
                }
            }
            Spacer(
                modifier = Modifier.padding(vertical = 8.dp)
            )

        }

    }




}

@Composable
fun HeaderRow(
    headerTitle: String,
    logo: Int,              // logos should be painterResource?
    otherLogo: Int
) {
    Row() {
        LargeText(text = headerTitle)
        // Icon(painter = , contentDescription = )
    }


}


@SuppressLint("UnusedTransitionTargetStateParameter")
@ExperimentalAnimationApi
@Composable
fun ExpandableRow(
    title: String,
    quizDescriptions: List<String>
) {
    // expanded toggles between true and false, triggering all the animations
    val expanded = remember { mutableStateOf(false) }
    val animationDuration = 1000

    val transitionState = remember {
        MutableTransitionState(expanded.value).apply {
            targetState = !expanded.value
        }
    }
    val transition = updateTransition(
        targetState = transitionState,
        label = "expandable transition"
    )

    // set parameters for expand animations
    // all animations happen over one second
    val backgroundColor by transition.animateColor({
        tween(durationMillis = animationDuration)
    }, label = "animate background color") {
        if (expanded.value)
            MaterialTheme.colors.secondary
        else
            MaterialTheme.colors.primary
    }

    val textColor by transition.animateColor({
        tween(durationMillis = animationDuration)
    }, label = "animate text color") {
        if (expanded.value)
            MaterialTheme.colors.primary
        else
            MaterialTheme.colors.onPrimary
    }

    val rowPaddingHorizontal by transition.animateDp({
        tween(durationMillis = animationDuration)
    }, label = "row padding") {
        if (expanded.value)
            8.dp
        else
            12.dp
    }

    val cardElevation by transition.animateDp({
        tween(durationMillis = animationDuration)
    }, label = "") {
        if (expanded.value)
            24.dp
        else
            4.dp
    }

    val rowRoundedCorners by transition.animateDp({
        tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        )
    }, label = "") {
        if (expanded.value)
            8.dp
        else
            16.dp
    }

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = animationDuration)
    }, label = "") {
        if (expanded.value)
            0f
        else
            180f
    }

    // animate content with the animation vals defined above
    Card(
        backgroundColor  = backgroundColor,
        contentColor = MaterialTheme.colors.primary,
        elevation = cardElevation,
        shape = RoundedCornerShape(rowRoundedCorners),
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(
                horizontal = rowPaddingHorizontal,
                //vertical = 8.dp
            )
    ) {

        Column(Modifier.clickable(onClick = { expanded.value = !expanded.value })) {
            CenteredContentRow {
                LargeText(
                    text = title,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                Icon(Icons.Rounded.PlayArrow,       // change to some music note
                    "",
                    modifier = Modifier.rotate(arrowRotationDegree)
                )
            }
            ExpandableContent(
                visible = expanded.value,
                initialVisibility = expanded.value,
                quizDescriptions = quizDescriptions
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ExpandableContent(
    visible: Boolean = true,
    initialVisibility: Boolean = false,
    quizDescriptions: List<String>
) {
    val animationDuration = 500
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = animationDuration,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(animationDuration))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = animationDuration,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(animationDuration))
    }
    AnimatedVisibility(
        visible = visible,
        initiallyVisible = initialVisibility,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            quizDescriptions.forEach { quizDescription ->
                CenteredContentRow {
                    Icon(Icons.Rounded.PlayArrow, "")     // music note icon
                    Text(
                        text = quizDescription,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}