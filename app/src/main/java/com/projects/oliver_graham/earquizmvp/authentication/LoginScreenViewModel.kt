package com.projects.oliver_graham.earquizmvp.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginScreenViewModel(
    private val navController: NavigationController,
    private val firebaseController: FirebaseController
    ): ViewModel() {

    val emailTextField: MutableState<String> = mutableStateOf("")
    val passwordTextField: MutableState<String> = mutableStateOf("")
    val loginButtonEnabled: MutableState<Boolean> = mutableStateOf(false)

    fun enableLoginButton() {
        loginButtonEnabled.value =
            emailTextField.value.isNotEmpty() && passwordTextField.value.length > 5
    }

    fun onLoginButtonClick() = viewModelScope.launch { ->

        withContext(Dispatchers.IO) { ->
            firebaseController.signInWithEmailAndPassword(
                emailTextField.value,
                passwordTextField.value
            )
            if (firebaseController.isUserLoggedIn())
                navController.navHomeScreenPopBackstack()

            // else
            // TODO: Display error message (Toast?)
        }
    }

    // Google and Facebook verify?

    // go to account creation screen
    fun createButtonClick() {
        navController.navCreateAccountScreen()
    }

    fun onSkipButtonClick() {
        navController.navHomeScreenPopBackstack()
    }
}

/* // TODO: Error validation for textfields
var text by rememberSaveable { mutableStateOf("") }
var isError by rememberSaveable { mutableStateOf(false) }

fun validate(text: String) {
    isError = /* .... */
}



Column {
    TextField(
        value = text,
        onValueChange = {
            text = it
            isError = false
        },
        trailingIcon = {
            if (isError)
                Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
        },
        singleLine = true,
        isError = isError,
        keyboardActions = KeyboardActions { validate(text) },
    )
    if (isError) {
        Text(
            text = "Error message",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}*/
