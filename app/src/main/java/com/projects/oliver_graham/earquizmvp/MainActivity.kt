/**
 *  Author:      Oliver Graham
 *  Email:       olivergraham916@gmail.com
 *  Please note: Sounds played when running on the emulator are choppy, but are
 *  perfectly smooth when running on a real device
 */

package com.projects.oliver_graham.earquizmvp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.projects.oliver_graham.earquizmvp.authentication.CreateAccountScreenViewModel
import com.projects.oliver_graham.earquizmvp.authentication.LoginScreenViewModel
import com.projects.oliver_graham.earquizmvp.homescreen.HomeScreenViewModel
import com.projects.oliver_graham.earquizmvp.leaderboardscreen.LeaderboardScreenViewModel
import com.projects.oliver_graham.earquizmvp.navigation.MainNavigation
import com.projects.oliver_graham.earquizmvp.quizscreen.QuizScreenViewModel
import com.projects.oliver_graham.earquizmvp.sounds.SoundPlayer
import com.projects.oliver_graham.earquizmvp.ui.theme.EarQuizMVPTheme

class MainActivity : ComponentActivity() {

    // TODO: Create viewModel factories
    /*private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    private val loginScreenViewModel: LoginScreenViewModel by viewModels()*/
       // LoginScreenViewModel(navWrapper, firebaseController)
/*
    val createAccountScreenViewModel = remember {
        CreateAccountScreenViewModel(navWrapper, firebaseController)
    }

    val quizScreenViewModel = remember {
        QuizScreenViewModel(navWrapper, firebaseController, quizController, musicTheory, soundPlayer)
    }

    val homeScreenViewModel = remember {
        HomeScreenViewModel(navWrapper, quizController, quizScreenViewModel)
    }

    val leaderboardScreenViewModel = remember {
        LeaderboardScreenViewModel(navWrapper, firebaseController)
    }*/

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EarQuizMVPTheme {

                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor (color = MaterialTheme.colors.primary)
                systemUiController.setNavigationBarColor(color = MaterialTheme.colors.background)

                // begin loading all sounds into SoundPlayer on app start-up
                MainNavigation(context = application, soundPlayer = SoundPlayer(application))
            }
        }
    }
}
