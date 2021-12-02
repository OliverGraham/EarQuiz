package com.projects.oliver_graham.earquizmvp.homescreen

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.projects.oliver_graham.earquizmvp.ui.CenteredContentRow
import com.projects.oliver_graham.earquizmvp.ui.CircularIconButton
import com.projects.oliver_graham.earquizmvp.ui.LargeText

// TODO: Refactor this screen

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxHeight()

    ){ ->
        // make an expandable quiz description, with button, for every description in the repo
        itemsIndexed(viewModel.quizDescriptions) { index, quizDescription ->
            Spacer(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) { ->

                ExpandableRow(                   // change name to ExpandableWideCard?
                    title = quizDescription.title,
                    quizDescriptions = quizDescription.descriptions
                )

                // only intervals quiz (index 0) is available, so disable other buttons
                val isIconButtonEnabled = remember { mutableStateOf(value = index == 0) }

                CircularIconButton(
                    onClick = { viewModel.onQuizButtonClick() },
                    icon = Icons.Default.ArrowForward,
                    mutableEnabled = isIconButtonEnabled,
                    buttonBackgroundColor =     // gray background if button is disabled
                        if (isIconButtonEnabled.value) Color.Unspecified
                        else Color(color = 0xFFB0BEC5)      // BlueGray200
                )
            }
            Spacer(
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

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
        MutableTransitionState(expanded.value).apply { ->
            targetState = !expanded.value
        }
    }
    val transition = updateTransition(
        targetState = transitionState,
        label = "expandable transition"
    )

    // set parameters for expand animations
    // all animations happen over one second
    val backgroundColor by transition.animateColor({ ->
        tween(durationMillis = animationDuration)
    }, label = "animate background color") {
        if (expanded.value)
            MaterialTheme.colors.secondary
        else
            MaterialTheme.colors.primary
    }

    val textColor by transition.animateColor({ ->
        tween(durationMillis = animationDuration)
    }, label = "animate text color") {
        if (expanded.value)
            MaterialTheme.colors.primary
        else
            MaterialTheme.colors.onPrimary
    }

    val rowPaddingHorizontal by transition.animateDp({ ->
        tween(durationMillis = animationDuration)
    }, label = "row padding") {
        if (expanded.value)
            8.dp
        else
            12.dp
    }

    val cardElevation by transition.animateDp({ ->
        tween(durationMillis = animationDuration)
    }, label = "") {
        if (expanded.value)
            24.dp
        else
            4.dp
    }

    val rowRoundedCorners by transition.animateDp({ ->
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

    val arrowRotationDegree by transition.animateFloat({ ->
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
            .fillMaxWidth(fraction = 0.75f)
            .padding(horizontal = rowPaddingHorizontal)
            .alpha(alpha = .9f)
    ) {

        Column(Modifier.clickable(onClick = { expanded.value = !expanded.value })) { ->
            CenteredContentRow { ->
                LargeText(
                    text = title,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                Icon(Icons.Rounded.MusicNote,       // change to some music note
                    contentDescription = "",
                    modifier = Modifier.rotate(arrowRotationDegree),
                    tint =  textColor

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
        Column(modifier = Modifier.padding(8.dp)) { ->
            quizDescriptions.forEach { quizDescription ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    horizontalArrangement = Arrangement.Start
                ) { ->
                    Icon(Icons.Rounded.MusicNote, contentDescription = "")
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = quizDescription,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}