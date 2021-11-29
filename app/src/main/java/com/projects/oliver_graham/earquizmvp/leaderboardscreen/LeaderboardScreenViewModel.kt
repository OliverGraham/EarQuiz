package com.projects.oliver_graham.earquizmvp.leaderboardscreen

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.data.User
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import kotlinx.coroutines.flow.StateFlow

class LeaderboardScreenViewModel(
    private val navController: NavigationController,
    private val firebaseController: FirebaseController
) : ViewModel() {

    // get flow of Users from firestore - emits changes that will cause UI to update
    val users: StateFlow<List<User>>
        get() = firebaseController.getUsers()

}