package com.projects.oliver_graham.earquizmvp.leaderboardscreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.projects.oliver_graham.earquizmvp.data.User
import com.projects.oliver_graham.earquizmvp.homescreen.ExpandableRow


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun LeaderboardScreen(viewModel: LeaderboardScreenViewModel) {

    val users = viewModel.sortedUsers
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState
    ){ ->
        items(
            items = users.value,
            key = { user: User -> user.uid}
        ) { user ->

            Spacer(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) { ->
                ExpandableRow(                   // change name to ExpandableWideCard?
                    title = user.userName,
                    quizDescriptions = listOf(
                        "Correct answers: ", user.correctAnswers.toString(),
                        "Incorrect answers: ", user.incorrectAnswers.toString())
                )
            }
            Spacer(
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

    }


}
