package com.projects.oliver_graham.earquizmvp.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.projects.oliver_graham.earquizmvp.data.User
import com.projects.oliver_graham.earquizmvp.navigation.Screen
import kotlinx.coroutines.launch

class CreateAccountScreenViewModel(
    private val navController: NavController,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore   // create user when account is created - for leaderboard
    ): ViewModel() {

    val usernameTextField: MutableState<String> = mutableStateOf("")
    val emailTextField: MutableState<String> = mutableStateOf("")
    val passwordTextField: MutableState<String> = mutableStateOf("")
    val confirmPasswordTextField: MutableState<String> = mutableStateOf("")
    val createAccountButtonEnabled: MutableState<Boolean> = mutableStateOf(false)

    // TODO: Create account with username?
    fun enableCreateAccountButton() {
        createAccountButtonEnabled.value =
            usernameTextField.value.isNotEmpty() &&
            emailTextField.value.isNotEmpty() &&
                    passwordTextField.value.length > 5 &&
                    confirmPasswordTextField.value.length > 5 &&
                    passwordTextField.value == confirmPasswordTextField.value
    }

    // Create user in authentication and create document in database
    fun createAccountButtonClick() = viewModelScope.launch { ->
        auth.createUserWithEmailAndPassword(emailTextField.value, passwordTextField.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // auth has a currentUser, it just can't believe it
                    firestore.collection("users").document(auth.currentUser!!.uid)
                        .set(User(
                                uid = auth.currentUser!!.uid,
                                userName = usernameTextField.value,
                                email = emailTextField.value
                            )
                        )

                    // TODO: add user to document in firestore
                    navController.navigate(Screen.HomeScreen.route) { ->

                        // so back button doesn't go back to login flow
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                } else {
                    // do something?
                }
            }
    }


}