package com.projects.oliver_graham.earquizmvp.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.projects.oliver_graham.earquizmvp.homescreen.HeaderRow
import com.projects.oliver_graham.earquizmvp.ui.*
import com.projects.oliver_graham.earquizmvp.ui.CenteredContentRow
import com.projects.oliver_graham.earquizmvp.R

@Composable
fun LoginScreen(viewModel: LoginScreenViewModel) {

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
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly

        ) { ->
            Image(
                painterResource(id = R.drawable.earquiz_mainlogo),      // TODO: need larger logo
                contentDescription = "EarQuiz Logo"
            )
        }
        Divider(modifier = Modifier.padding(8.dp))
        CenteredContentRow(padding = 1.dp) { ->
            GenericTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                text = viewModel.emailTextField.value,
                label = "Email",
                onTextChange = { text ->
                    viewModel.emailTextField.value = text
                    viewModel.enableLoginButton()
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
                trailingIcon = { EyeBallIcon(
                    onClick = { passwordVisibility.value = !passwordVisibility.value },
                    clicked = passwordVisibility.value
                )},
                onTextChange = { text ->
                    viewModel.passwordTextField.value = text
                    viewModel.enableLoginButton()
                }
            )
        }

        CenteredContentRow(padding = 8.dp) { ->
            LargeButton(
                onClick = { viewModel.onLoginButtonClick() },
                mutableEnabled = viewModel.loginButtonEnabled,
            ) { ->
                LargeText(text = "Login")
            }
        }
        CenteredContentRow(padding = 8.dp) { ->
            LargeButton(onClick = {  }) { ->
                LargeText(text = "(GOOGLE)")
            }
        }
        CenteredContentRow(padding = 8.dp) { ->
            LargeButton(onClick = {  }) { ->
                LargeText(text = "(FACEBOOK)")
            }
        }
        Divider(modifier = Modifier.padding(8.dp))
        CenteredContentRow(padding = 8.dp) { ->
            LargeButton(onClick = { viewModel.createButtonClick() }) { ->
                LargeText(text = "Create Account")
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) { ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) { ->

                MediumButton(onClick = { viewModel.onSkipButtonClick() }) {
                    LargeText(text = "Skip")
                    Icon(Icons.Rounded.PlayArrow, "")
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
        }

    }

}
