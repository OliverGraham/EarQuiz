package com.projects.oliver_graham.earquizmvp.leaderboardscreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.projects.oliver_graham.earquizmvp.data.User
import com.projects.oliver_graham.earquizmvp.ui.CenteredContentRow
import com.projects.oliver_graham.earquizmvp.ui.LargeText

@Composable
fun LeaderboardScreen(viewModel: LeaderboardScreenViewModel) {

    val users = viewModel.users.collectAsState()

    LazyColumn {
        items(
            items = users.value,
            key = { user: User -> user.uid}
        ) { user ->

            CenteredContentRow {
                LargeText(text = user.userName)
                LargeText(text = user.correctAnswers.toString())
                LargeText(text = user.incorrectAnswers.toString())
            }

        }
    }
}