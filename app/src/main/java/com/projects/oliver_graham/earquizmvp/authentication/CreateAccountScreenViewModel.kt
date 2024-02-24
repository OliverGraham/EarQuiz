package com.projects.oliver_graham.earquizmvp.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import kotlinx.coroutines.launch

class CreateAccountScreenViewModel(
    private val navController: NavigationController,
    private val firebaseController: FirebaseController
    ): ViewModel() {

    val usernameTextField: MutableState<String> = mutableStateOf(value = "")
    val emailTextField: MutableState<String> = mutableStateOf(value = "")
    val passwordTextField: MutableState<String> = mutableStateOf(value = "")
    val confirmPasswordTextField: MutableState<String> = mutableStateOf(value = "")
    val createAccountButtonEnabled: MutableState<Boolean> = mutableStateOf(value = false)

    fun enableCreateAccountButton() {
        createAccountButtonEnabled.value =
            usernameTextField.value.isNotEmpty() &&
            emailTextField.value.isNotEmpty() &&
                    passwordTextField.value.length > 5 &&
                    confirmPasswordTextField.value.length > 5 &&
                    passwordTextField.value == confirmPasswordTextField.value
    }

    // Create user after authentication and create document in database
    fun createAccountButtonClick() = viewModelScope.launch { ->
        firebaseController.createUserWithEmailAndPassword(
            usernameTextField.value, emailTextField.value, passwordTextField.value
        )
        navController.navHomeScreenNoBack()
    }
}