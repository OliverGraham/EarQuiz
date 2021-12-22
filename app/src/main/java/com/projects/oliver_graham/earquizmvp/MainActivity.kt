package com.projects.oliver_graham.earquizmvp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.projects.oliver_graham.earquizmvp.navigation.MainNavigation
import com.projects.oliver_graham.earquizmvp.sounds.SoundPlayer
import com.projects.oliver_graham.earquizmvp.ui.theme.EarQuizMVPTheme

class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            EarQuizMVPTheme {

                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor (
                    color = MaterialTheme.colors.primary
                )
                systemUiController.setNavigationBarColor(
                    color = MaterialTheme.colors.background
                )
                MainNavigation(context = this, SoundPlayer(application))
            }
        }
    }
}
