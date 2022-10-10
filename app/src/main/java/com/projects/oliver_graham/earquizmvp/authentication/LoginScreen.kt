package com.projects.oliver_graham.earquizmvp.authentication


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.projects.oliver_graham.earquizmvp.R
import com.projects.oliver_graham.earquizmvp.ui.*

@Composable
fun LoginScreen(viewModel: LoginScreenViewModel) {

    val focusManager = LocalFocusManager.current
    val passwordVisibility = remember { mutableStateOf(value = false) }

    val googleSignInActivity = rememberLauncherForActivityResult(contract =
        ActivityResultContracts.StartActivityForResult()) { activityResult ->
        viewModel.completeGoogleSignIn(activityResult.data)
    }

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
            ThemeButton(
                text = "Login",
                onButtonClick = { viewModel.onLoginButtonClick() },
                mutableEnabled = viewModel.loginButtonEnabled
            )
        }
        CenteredContentRow(padding = 8.dp) { ->
           LargeButton(onClick = {
                googleSignInActivity.launch(viewModel.getGoogleSignInIntent())
            }) { ->
               Image(painterResource(
                   id = R.drawable.googleg_standard_color_18),
                   contentDescription = "Google's 'G'"
               )
               LargeText(text = "oogle")
            }
        }
        CenteredContentRow(padding = 8.dp) { ->
            val falseFacebook = remember { mutableStateOf(value = false) }
            ThemeButton(
                text = "(Facebook)",
                onButtonClick = { /* Add later */ },
                mutableEnabled = falseFacebook
            )
        }
        Divider(modifier = Modifier.padding(8.dp))
        CenteredContentRow(padding = 8.dp) { ->
            LargeButton(onClick = { viewModel.createButtonClick() }) { ->
                LargeText(text = "Create Account")
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))

        Column(
            verticalArrangement = Arrangement.Bottom
        ) { ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) { ->

                MediumButton(onClick = { viewModel.onSkipButtonClick() }) { ->
                    LargeText(text = "Skip")
                    Icon(Icons.Rounded.PlayArrow, contentDescription = "")
                }
            }
        }

    }

}
