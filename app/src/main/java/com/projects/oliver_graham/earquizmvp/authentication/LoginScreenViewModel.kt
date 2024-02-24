package com.projects.oliver_graham.earquizmvp.authentication

import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.projects.oliver_graham.earquizmvp.data.FirebaseController
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController

class LoginScreenViewModel(
    private val navController: NavigationController,
    private val firebaseController: FirebaseController
    ) : ViewModel() {

    val emailTextField: MutableState<String> = mutableStateOf(value = "")
    val passwordTextField: MutableState<String> = mutableStateOf(value = "")
    val loginButtonEnabled: MutableState<Boolean> = mutableStateOf(value = false)

    fun enableLoginButton() {
        loginButtonEnabled.value =
            emailTextField.value.isNotEmpty() && passwordTextField.value.length > 5
    }

    fun onLoginButtonClick() {
        firebaseController.signInWithEmailAndPassword(
            emailTextField.value,
            passwordTextField.value
        )
        if (firebaseController.isUserLoggedIn())
            navController.navHomeScreenPopBackstack()
    }

    // (Facebook sign in will be added)
    fun getGoogleSignInIntent() = firebaseController.getGoogleSignInIntent()
    fun completeGoogleSignIn(intent: Intent?) = firebaseController.completeGoogleSignIn(intent)

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
