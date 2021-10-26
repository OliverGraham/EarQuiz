package com.projects.oliver_graham.earquizmvp.quizpage

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.sharp.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.projects.oliver_graham.earquizmvp.R
import com.projects.oliver_graham.earquizmvp.data.QuizQuestion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun QuizPage(viewModel: QuizPageViewModel) {

    val scope = rememberCoroutineScope()

    // entire body
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // top row
        Row(

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LargeText(
                    text = viewModel.quizName,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.padding(4.dp))

                LargeText(
                    text = "${viewModel.questionNumber.value}/10",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(4.dp))

                LargeText(
                    text = "+ ${viewModel.correctUserAnswers.value}",
                    color = Color(0xFF689F38)
                )
                Spacer(modifier = Modifier.padding(4.dp))

                LargeText(
                    text = "- ${viewModel.incorrectUserAnswers.value}",
                    color = Color.Red
                )
                Spacer(modifier = Modifier.padding(4.dp))

                Icon(
                    Icons.Rounded.Settings,
                    "",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Divider(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colors.primaryVariant
        )

        // Play interval button and count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LargeButton(
                onClick = {
                    scope.launch {
                        viewModel.playSound()
                        viewModel.playButtonEnabled.value = false

                        // set timer
                        delay(4500)

                        viewModel.playButtonEnabled.value = true
                    }
                },
                mutableEnabled = viewModel.playButtonEnabled,
                content = {
                    LargeText(
                        text = "Play Interval",
                        fontSize = 20.sp
                    )
                    Icon(Icons.Rounded.PlayArrow, "")
                }
            )
            LargeText(
                text = viewModel.numberOfIntervalTaps.value.toString(),
                color = if (isSystemInDarkTheme())
                    MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary,
                fontSize = 36.sp
            )
        }

        //BackGroundImage {
            AnswerGroup(
                submitButtonEnabled = viewModel.submitButtonEnabled,
                userChoice = viewModel.currentUserChoice,
                viewModel.radioGroup,
            )
       // }

/*        AnswerGroup(
            submitButtonEnabled = viewModel.submitButtonEnabled,
            userChoice = viewModel.currentUserChoice,
            viewModel.radioGroup,
        )*/

        CenteredContentRow {
            LargeButton(
                onClick = {
                    viewModel.submitAnswer(viewModel.currentUserChoice.value)
                },
                mutableEnabled = viewModel.submitButtonEnabled

            ) {
                LargeText(
                    text = "Submit Answer",
                    fontSize = 20.sp
                )
                Icon(Icons.Rounded.KeyboardArrowUp, "")
            }
        }
        //DialogThing()

    } // end of column

}

@Composable
fun BackGroundImage(
    answerGroup: @Composable () -> Unit
) {
    // You can add background image to a Box just by placing it under all other views.
    // And matchParentSize modifier will stretch it to the parent size.
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "",
            contentScale = ContentScale.FillBounds, // or some other scale
            modifier = Modifier.matchParentSize()
        )
       answerGroup()
    }
}

/*@Composable
fun BackGroundImage() {
    // You can add background image to a Box just by placing it under all other views.
    // And matchParentSize modifier will stretch it to the parent size.
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "",
            contentScale = ContentScale.FillBounds, // or some other scale
            modifier = Modifier.matchParentSize()
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            IconButton(
                onClick = { *//*...*//* },
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .fillMaxWidth()
            ) {
*//*                Icon(
                    Icons.Filled.Close,
                    contentDescription = "stringResource(id = R.string.close)",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )*//*
            }
        }
    }
}*/


@Composable
fun LargeButton(
    modifier: Modifier = Modifier,
    mutableEnabled: MutableState<Boolean> = mutableStateOf(true),
    onClick: () -> Unit,
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = elevation(4.dp, 16.dp),
    shape: Shape = RoundedCornerShape(50),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(width = 220.dp, height = 60.dp),
        elevation = elevation,
        enabled = mutableEnabled.value,
        border = border,
        shape = shape,
        colors = colors,
        content = content,
    )
}


@Composable
fun CenteredContentRow(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.HorizontalOrVertical = Arrangement.SpaceEvenly,
    rowContent: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        content = rowContent
    )
}


@Composable
fun AnswerGroup(
    submitButtonEnabled: MutableState<Boolean>,
    userChoice: MutableState<String>,
    answerGroup: SnapshotStateList<QuizQuestion>
) {

    Column(
        modifier = Modifier
           // .fillMaxWidth()
          //  .fillMaxHeight(.5f)
            //  .height(200.dp)     // could fillMaxHeight, when things are below and add padding or something
            .padding(16.dp)
           // .border(4.dp, MaterialTheme.colors.primaryVariant)
    ) {

        for (answer in answerGroup) {

          //  Divider(modifier = Modifier.size(4.dp),
          //      thickness = 0.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),

               // horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LargeText(
                        text = answer.text,
                        fontSize = 24.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    horizontalAlignment = Alignment.End
                ) {

                    RadioButton(
                        selected = userChoice.value == answer.id,
                        onClick = {
                            userChoice.value = answer.id
                            submitButtonEnabled.value = true
                        },
                        modifier = Modifier.scale(1.75f, 1.75f)

                        //onClick = { selected.value = answer.id }
                    )
                }
            }
            //DialogThing()
        }
    }
}

@Composable
fun DialogThing() {
    val openDialog = remember { mutableStateOf(true) }
    var text by remember { mutableStateOf("") }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Title")
            },
            text = {
                Column() {
                    TextField(
                        value = text,
                        onValueChange = { text = it }
                    )
                    Text("Custom Text")
                    Checkbox(checked = false, onCheckedChange = {})
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialog.value = false }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}


@Composable
fun LargeText(
    text: String,
    fontFamily: FontFamily? = null,
    fontSize: TextUnit = 22.sp,
    fontWeight: FontWeight? = null,
    color: Color = Color.Unspecified

) {
    Text(
        text = text,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
    )
}



/*@Composable
fun displayRadioGroup(){
    var selected by remember { mutableStateOf("Male") }
    Row {
        RadioButton(selected = selected == "Male", onClick = { selected = "Male" })
        Text(
            text = "Male",
            modifier = Modifier.clickable(onClick = { selected = "Male" }).padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        RadioButton(selected = selected == "Female", onClick = { selected = "Female" })
        Text(
            text = "Female",
            modifier = Modifier.clickable(onClick = { selected = "Female" }).padding(start = 4.dp)
        )
    }
}*/