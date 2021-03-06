package com.projects.oliver_graham.earquizmvp.leaderboardscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.data.User
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LeaderboardScreenViewModel(
    private val navController: NavigationController,
    private val firebaseController: FirebaseController
) : ViewModel() {

    // get flow of Users from firestore - emits changes that will cause UI to update
    private val users: StateFlow<List<User>>
        get() = firebaseController.getUsers()

    val sortedUsers: MutableState<List<User>> = mutableStateOf(mutableListOf())

    init {
        orderByCorrectAnswers()
    }

    private fun orderByCorrectAnswers(ascending: Boolean = true) = viewModelScope.launch { ->
        users.collect { userList ->

            val sortedList = userList.toMutableList()
            sortedList.sortWith(compareBy { it.correctAnswers })
            sortedList.reverse()
            sortedUsers.value = sortedList
        }

    }

}