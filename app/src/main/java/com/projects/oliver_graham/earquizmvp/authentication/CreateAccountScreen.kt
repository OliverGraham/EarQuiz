package com.projects.oliver_graham.earquizmvp.authentication

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.projects.oliver_graham.earquizmvp.ui.*

// TODO: functions for each element
//       Password field and username field could be generic

@Composable
fun CreateAccountScreen(viewModel: CreateAccountScreenViewModel) {

    val focusManager = LocalFocusManager.current
    val passwordVisibility = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { ->
                detectTapGestures(onTap = { _ ->
                    focusManager.clearFocus()           // tap outside keyboard to dismiss
                    passwordVisibility.value = false
                })
            }
    ) { ->
        CenteredContentRow(padding = 1.dp) { ->
            GenericTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                text = viewModel.usernameTextField.value,
                label = "Username",
                onTextChange = { text ->
                    viewModel.usernameTextField.value = text
                    viewModel.enableCreateAccountButton()
                }
            )
        }
        CenteredContentRow(padding = 1.dp) { ->
            GenericTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                text = viewModel.emailTextField.value,
                label = "Email",
                onTextChange = { text ->
                    viewModel.emailTextField.value = text
                    viewModel.enableCreateAccountButton()
                }
            )
        }

        CenteredContentRow(padding = 1.dp) { ->
            GenericTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation =
                if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                text = viewModel.passwordTextField.value,
                label = "Password",
                trailingIcon = {
                    EyeBallIcon(
                        onClick = { passwordVisibility.value = !passwordVisibility.value },
                        clicked = passwordVisibility.value
                    )
                },
                onTextChange = { text ->
                    viewModel.passwordTextField.value = text
                    viewModel.enableCreateAccountButton()
                }
            )
        }

        CenteredContentRow(padding = 1.dp) { ->
            GenericTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation =
                if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                text = viewModel.confirmPasswordTextField.value,
                label = "Confirm Password",
                trailingIcon = {
                    EyeBallIcon(
                        onClick = { passwordVisibility.value = !passwordVisibility.value },
                        clicked = passwordVisibility.value
                    )
                },
                onTextChange = { text ->
                    viewModel.confirmPasswordTextField.value = text
                    viewModel.enableCreateAccountButton()
                }
            )
        }

        CenteredContentRow(padding = 8.dp) { ->
            LargeButton(
                onClick = { viewModel.createAccountButtonClick() },
                mutableEnabled = viewModel.createAccountButtonEnabled,
            ) { ->
                LargeText(text = "Create")
            }
        }
    }
}