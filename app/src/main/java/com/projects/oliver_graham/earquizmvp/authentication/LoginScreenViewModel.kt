package com.projects.oliver_graham.earquizmvp.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.firebase.auth.FirebaseAuth
import com.projects.oliver_graham.earquizmvp.navigation.Screen
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val navController: NavController,
    private val auth: FirebaseAuth
    ): ViewModel() {

    val emailTextField: MutableState<String> = mutableStateOf("")
    val passwordTextField: MutableState<String> = mutableStateOf("")
    val loginButtonEnabled: MutableState<Boolean> = mutableStateOf(false)

    fun enableLoginButton() {
        loginButtonEnabled.value =
            emailTextField.value.isNotEmpty() && passwordTextField.value.length > 5
    }

    fun onLoginButtonClick() = viewModelScope.launch { ->

        auth.signInWithEmailAndPassword(emailTextField.value, passwordTextField.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.popBackStack()
                    navController.navigate(Screen.HomeScreen.route)
                } else {
                    // do something?
                }
            }

    }

    // Google and Facebook verify?

    // go to account creation screen
    fun createButtonClick() {
        navController.navigate(Screen.CreateAccountScreen.route)
    }

    // skip
    fun onSkipButtonClick() {
        navController.popBackStack()
        navController.navigate(Screen.HomeScreen.route)
    }
}