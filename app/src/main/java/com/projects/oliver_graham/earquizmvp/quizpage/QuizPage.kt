package com.projects.oliver_graham.earquizmvp.quizpage

import android.graphics.Color.alpha
import android.graphics.drawable.Icon
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.projects.oliver_graham.earquizmvp.R
import com.projects.oliver_graham.earquizmvp.ui.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// BEFORE MOVING ON, REFACTOR TO THIS STYLE:
//
// @Composable
//fun TopLevelView(
//    viewModel: ViewModel
//) {
//   LowerLevelView(
//       onButtonClick = viewModel::onButtonClick
//   )
//}
//
//@Composable
//fun LowerLevelView(
//    onButtonClick: (params: Any) -> Unit
//) {
//    val params = // get your params somehow
//    Button(onClick = onSomeActionTaken(params))
//}

// DID YOU DO IT? DO IT TOMORROW (HALLOWEEN)


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
                    text = "${viewModel.questionNumber.value}/${viewModel.totalQuestions}",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(4.dp))

                LargeText(
                    text = "+ ${viewModel.correctUserAnswers.value}",
                    color = Color(0xFF689F38)   // regular green too bright
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
                onClick = { viewModel.playSound() },
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

        Column(modifier = Modifier.padding(16.dp)) {
            for (quizQuestion in viewModel.radioGroup) {
                TextAndRadio(
                    text = quizQuestion.text,
                    radioID = quizQuestion.id,
                    radioChoice = viewModel.currentUserChoice,
                    enableOtherUI = viewModel.submitButtonEnabled
                )
            }
        }

        var showDialog by remember { mutableStateOf(false) }
        CenteredContentRow {
            LargeButton(
                onClick = { showDialog = !showDialog },
                mutableEnabled = viewModel.submitButtonEnabled
            ) {
                LargeText(
                    text = "Submit Answer",
                    fontSize = 20.sp
                )
                Icon(Icons.Rounded.KeyboardArrowUp, "")
            }
        }
        var showFinishedDialog by remember { mutableStateOf(false) }
        if (showDialog) {

            val outcome = viewModel.determineOutcome()

            var userChoiceText: String? = null
            val title: String
            val scoreSign: String
            val scoreColor: Color

            if (outcome) {
                title = "Correct"
                scoreSign = "+"
                scoreColor = Color(0xFF689F38)
            } else {
                title = "Incorrect"
                scoreSign = "-"
                scoreColor = Color.Red
                userChoiceText = viewModel.convertUserChoiceToText()
            }

            AnswerDialog(
                dismissRequest = {

                    showDialog = !showDialog

                    if (outcome)
                        viewModel.correctUserAnswers.value++
                    else
                        viewModel.incorrectUserAnswers.value++

                    if (viewModel.questionNumber.value == viewModel.totalQuestions)
                        // next dialog
                        // write to database
                        // there, go to HomePage
                        showFinishedDialog = !showFinishedDialog
                    else {
                        viewModel.questionNumber.value++
                        viewModel.resetQuizPage()
                    }
                },
                resultTitle = title,
                scoreSign = scoreSign,
                scoreColor = scoreColor,
                correctAnswerText = viewModel.currentCorrectAnswer.value.text,
                incorrectAnswerText = userChoiceText,
                playSound = { viewModel.playSound(false) },
                playButtonEnabled = viewModel.playButtonEnabled,
                nextButtonEnabled = viewModel.nextButtonEnabled,
                sheetMusicPainterResourceId1 = if (outcome) R.drawable.p5ae else R.drawable.p5ae,
                //  sheetMusicPainterResourceId2 = R.drawable.d_sharp,
                //    sheetMusicPainterResourceId3 = R.drawable.b_4,
                buttonText = "Next",
                buttonIcon = { Icon(Icons.Default.ArrowForward, "") }
            )

        }

        if (showFinishedDialog) {
            FinishedDialog(
                dismissRequest = { /*TODO*/ },
                title = "Finished! ${viewModel.questionNumber.value}/${viewModel.totalQuestions}",
                correctText = "You got ${viewModel.correctUserAnswers.value} correct",
                incorrectText = "You got ${viewModel.incorrectUserAnswers.value} incorrect",
                numberOfSoundsPlayedText =
                "You pressed the interval button ${viewModel.numberOfIntervalTaps.value} times",
                navButton1 = { /*TODO*/ },
                navButton2 = { /*TODO*/ }
            )
        }


    } // end of column
}


@Composable
fun FinishedDialog(
    dismissRequest: () -> Unit,
    title: String,
    correctText: String,
    incorrectText: String,
    numberOfSoundsPlayedText: String,
    navButton1: @Composable () -> Unit,
    navButton2: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = { dismissRequest() },
        title = {
            CenteredContentRow {
                LargeText(
                    text = title,
                    fontSize = 28.sp
                )
            }
        },
        text = {
            Column {
                LargeText(
                    text = correctText
                )
                Spacer(modifier = Modifier.padding(4.dp))
                LargeText(
                    text = incorrectText
                )
                Spacer(modifier = Modifier.padding(4.dp))
                LargeText(
                    text = numberOfSoundsPlayedText
                )
                Spacer(modifier = Modifier.padding(4.dp))

            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)

            ) {
                MediumButton(
                    onClick = {  },
                    //mutableEnabled = ,
                    content = {
                        LargeText(
                            text = "Home",
                            fontSize = 12.sp
                        )
                        Icon(Icons.Rounded.Home, "")
                    }
                )
                Spacer(modifier = Modifier.padding(4.dp))
                MediumButton(
                    onClick = {  },
                    //mutableEnabled = ,
                    content = {
                        LargeText(
                            text = "Leaderboard",
                            fontSize = 12.sp
                        )
                        Icon(Icons.Rounded.Notifications, "")
                    }
                )
            }
        }
    )



}

// Strictly for right and wrong answers in this quiz page
@Composable
fun AnswerDialog(
    dismissRequest: () -> Unit,
    resultTitle: String,
    scoreSign: String,
    scoreColor: Color,
    correctAnswerText: String,
    incorrectAnswerText: String?,
    playSound: (Boolean) -> Unit,
    playButtonEnabled: MutableState<Boolean>,
    nextButtonEnabled: MutableState<Boolean>,
    sheetMusicPainterResourceId1: Int,    // for now
    sheetMusicPainterResourceId2: Int = 0,    // for now
    sheetMusicPainterResourceId3: Int = 0,    // for now
    buttonText: String,
    buttonIcon: @Composable (() -> Unit)
) {
    nextButtonEnabled.value = true
    AlertDialog(
        onDismissRequest = {
            nextButtonEnabled.value = false
            dismissRequest()

                           },
        title = {
            CenteredContentRow() {
                LargeText(text = resultTitle)
                LargeText(
                    text = "${scoreSign}1",
                    color = scoreColor
                )
            }
        },
        text = {
               Column {
                   TextWithLeadingIcon(
                       text = correctAnswerText,
                       Icons.Default.Check,
                       tint = Color(0xFF689F38)
                   )
                   if (incorrectAnswerText != null) {
                       TextWithLeadingIcon(
                           text = incorrectAnswerText,
                           Icons.Default.Close,
                           tint = scoreColor
                       )
                   }

                   CenteredContentRow {
                       MediumButton(
                           onClick = { playSound(false) },
                           mutableEnabled = playButtonEnabled,
                           content = {
                               LargeText(
                                   text = "Play Interval",
                                   fontSize = 16.sp
                               )
                               Icon(Icons.Rounded.PlayArrow, "")
                           }
                       )
                   }

                   CenteredContentRow( // CHANG BACK TO ROW
                       horizontalArrangement = Arrangement.Center
                   ) {
                       Image(
                           painter = painterResource(id = sheetMusicPainterResourceId1),
                           contentDescription = null
                       )
/*                       Image(
                           painter = painterResource(id = sheetMusicPainterResourceId2),
                           contentDescription = null
                       )
                       Image(
                           painter = painterResource(id = sheetMusicPainterResourceId3),
                           contentDescription = null
                       )*/
                   }
               }
        },        
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.End,
            ) {

                MediumButton(
                    onClick = {
                        nextButtonEnabled.value = false
                        dismissRequest()

                    }

                    ,
                    mutableEnabled = nextButtonEnabled
                ) {
                    LargeText(text = buttonText)
                    buttonIcon()
                }
            }
        }
    )
}




/*@Composable
fun PlaySoundButton(
    playSound: () -> Unit,
    playButtonEnabled: MutableState<Boolean>,
    buttonText: String,

) {
    val scope = rememberCoroutineScope()
    LargeButton(
        onClick = {
            scope.launch {
                playSound()
                playButtonEnabled.value = false
                delay(4500)
                playButtonEnabled.value = true
            }
        },
        mutableEnabled = playButtonEnabled,
        content = {
            LargeText(
                text = buttonText,
                fontSize = 20.sp
            )
            Icon(Icons.Rounded.PlayArrow, "")
        }
    )
}*/
