package com.projects.oliver_graham.earquizmvp.quizscreen


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
import com.projects.oliver_graham.earquizmvp.data.QuizQuestion
import com.projects.oliver_graham.earquizmvp.ui.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun QuizScreen(viewModel: QuizScreenViewModel) {

    val scope = rememberCoroutineScope()

    // entire body
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) { ->
        TopRow(
            quizName = viewModel.quizName,
            questionNumber = viewModel.questionNumber.value,
            totalQuestions = viewModel.totalQuestions,
            correctUserAnswers = viewModel.correctUserAnswers.value,
            incorrectUserAnswers = viewModel.incorrectUserAnswers.value
        )
        Divider(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colors.primaryVariant
        )
        PlayIntervalButtonRow(
            playSound = { viewModel.playSound() },
            playButtonEnabled = viewModel.playButtonEnabled,
            numberOfIntervalTaps = viewModel.numberOfIntervalTaps.value
        )
        Column(modifier = Modifier.padding(16.dp)) { ->
            for (quizQuestion in viewModel.radioGroup) {
                TextAndRadio(
                    text = quizQuestion.text,
                    radioID = quizQuestion.id,
                    radioChoice = viewModel.currentUserChoice,
                    enableOtherUI = viewModel.submitButtonEnabled
                )
            }
        }

        CenteredContentRow { ->
            LargeButton(
                onClick = { viewModel.showAnswerDialog.value = !viewModel.showAnswerDialog.value },
                mutableEnabled = viewModel.submitButtonEnabled
            ) { ->
                LargeText(text = "Submit Answer", fontSize = 20.sp)
                Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = null)
            }
        }

        if (viewModel.showAnswerDialog.value) {
            val outcome = remember { viewModel.determineOutcome() }
            HandleAnswerDialog(
                outcome = outcome,
                convertUserChoiceToText = { viewModel.convertUserChoiceToText() },
                dismissRequest = { viewModel.answerDialogDismissRequest(outcome) },
                playSound = { viewModel.playSound(countTowardScore = false) },
                playButtonEnabled = viewModel.playButtonEnabled,
                nextButtonEnabled = viewModel.nextButtonEnabled,
                currentCorrectAnswer = viewModel.currentCorrectAnswer.value
            )
        }

        if (viewModel.showFinishedDialog.value) {
            FinishedDialog(
                dismissRequest = {
                    viewModel.resetQuizScreen()
                    viewModel.navToHomeScreen()
                },
                title = "Finished! ${viewModel.questionNumber.value}/${viewModel.totalQuestions}",
                correctText = "You got ${viewModel.correctUserAnswers.value} correct",
                incorrectText = "You got ${viewModel.incorrectUserAnswers.value} incorrect",
                numberOfSoundsPlayedText =
                "You pressed the interval button ${viewModel.numberOfIntervalTaps.value} times",
                onHomeButtonClick = {
                    viewModel.resetQuizScreen()
                    viewModel.navToHomeScreen()
               },
                onLeaderboardButtonClick = {
                    viewModel.resetQuizScreen()
                    viewModel.navToLeaderboardScreen()
                },
                navButton1 = { viewModel.navToHomeScreen() },
                navButton2 = { viewModel.navToLeaderboardScreen() }
            )
        }
    }
}

@Composable
private fun HandleAnswerDialog(
    outcome: Boolean,
    convertUserChoiceToText: () -> String,
    dismissRequest: (Boolean) -> Unit,
    playSound: (Boolean) -> Job,
    playButtonEnabled: MutableState<Boolean>,
    nextButtonEnabled: MutableState<Boolean>,
    currentCorrectAnswer: QuizQuestion
) {
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
        userChoiceText = convertUserChoiceToText()
    }

    AnswerDialog(
        dismissRequest = { dismissRequest(outcome) },
        resultTitle = title,
        scoreSign = scoreSign,
        scoreColor = scoreColor,
        correctAnswerText = currentCorrectAnswer.text,
        incorrectAnswerText = userChoiceText,
        playSound = { playSound(false) },
        playButtonEnabled = playButtonEnabled,
        nextButtonEnabled = nextButtonEnabled,
        clefsResourceId = currentCorrectAnswer.clefsImage,
        noteOneResourceId = currentCorrectAnswer.firstNote,
        noteTwoResourceId = currentCorrectAnswer.secondNote,
        buttonText = "Next",
        buttonIcon = { Icon(Icons.Default.ArrowForward, contentDescription = "") }
    )
}

// Strictly for right and wrong answers in this quiz page
@Composable
private fun AnswerDialog(
    dismissRequest: () -> Unit,
    resultTitle: String,
    scoreSign: String,
    scoreColor: Color,
    correctAnswerText: String,
    incorrectAnswerText: String?,
    playSound: (Boolean) -> Unit,
    playButtonEnabled: MutableState<Boolean>,
    nextButtonEnabled: MutableState<Boolean>,
    clefsResourceId: Int,
    noteOneResourceId: Int,
    noteTwoResourceId: Int,
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
            CenteredContentRow { ->
                LargeText(text = resultTitle)
                LargeText(
                    text = "${scoreSign}1",
                    color = scoreColor
                )
            }
        },
        text = {
            Column { ->
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

                CenteredContentRow { ->
                    MediumButton(
                        onClick = { playSound(false) },
                        mutableEnabled = playButtonEnabled,
                        content = { ->
                            LargeText(
                                text = "Play Interval",
                                fontSize = 16.sp
                            )
                            Icon(Icons.Rounded.PlayArrow, contentDescription = "")
                        }
                    )
                }

                CenteredContentRow(
                    horizontalArrangement = Arrangement.Center
                ) { ->
                    Image(
                        painter = painterResource(id = clefsResourceId),
                        contentDescription = null
                    )
                    Image(
                        painter = painterResource(id = noteOneResourceId),
                        contentDescription = null
                    )
                    Image(
                        painter = painterResource(id = noteTwoResourceId),
                        contentDescription = null
                    )
                }
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.End,
            ) { ->
                MediumButton(
                    onClick = {
                        nextButtonEnabled.value = false
                        dismissRequest()
                    },
                    mutableEnabled = nextButtonEnabled
                ) { ->
                    LargeText(text = buttonText)
                    buttonIcon()
                }
            }
        }
    )
}


@Composable
private fun FinishedDialog(
    dismissRequest: () -> Unit,
    title: String,
    correctText: String,
    incorrectText: String,
    numberOfSoundsPlayedText: String,
    onHomeButtonClick: () -> Unit,
    onLeaderboardButtonClick: () -> Unit,
    navButton1: @Composable () -> Unit,
    navButton2: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = { dismissRequest() },
        title = {
            CenteredContentRow { ->
                LargeText(
                    text = title,
                    fontSize = 28.sp
                )
            }
        },
        text = {
            Column { ->
                LargeText(text = correctText)
                Spacer(modifier = Modifier.padding(4.dp))

                LargeText(text = incorrectText)
                Spacer(modifier = Modifier.padding(4.dp))

                LargeText(text = numberOfSoundsPlayedText)
                Spacer(modifier = Modifier.padding(4.dp))
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) { ->
                MediumButton(
                    onClick = { onHomeButtonClick() },
                    //mutableEnabled = ,
                    content = { ->
                        LargeText(
                            text = "Home",
                            fontSize = 12.sp
                        )
                        Icon(Icons.Rounded.Home, contentDescription = "")
                    }
                )
                Spacer(modifier = Modifier.padding(4.dp))
                MediumButton(
                    onClick = { onLeaderboardButtonClick() },
                    //mutableEnabled = ,
                    content = { ->
                        LargeText(
                            text = "Leaderboard",
                            fontSize = 12.sp
                        )
                        Icon(Icons.Rounded.Notifications, contentDescription = "")
                    }
                )
            }
        }
    )
}

@Composable
private fun TopRow(
    quizName: String,
    questionNumber: Int,
    totalQuestions: Int,
    correctUserAnswers: Int,
    incorrectUserAnswers: Int
) {
    Row { ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) { ->
            LargeText(
                text = quizName,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.padding(4.dp))

            LargeText(
                text = "$questionNumber/$totalQuestions",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(4.dp))

            LargeText(
                text = "+ $correctUserAnswers",
                color = Color(0xFF689F38)   // regular green too bright
            )
            Spacer(modifier = Modifier.padding(4.dp))

            LargeText(
                text = "- $incorrectUserAnswers",
                color = Color.Red
            )
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
private fun PlayIntervalButtonRow(
    playSound: () -> Unit,
    playButtonEnabled: MutableState<Boolean>,
    numberOfIntervalTaps: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) { ->
        LargeButton(
            onClick = { playSound() },
            mutableEnabled = playButtonEnabled,
            content = {
                LargeText(
                    text = "Play Interval",
                    fontSize = 20.sp
                )
                Icon(Icons.Rounded.PlayArrow, contentDescription = "")
            }
        )
        LargeText(
            text = numberOfIntervalTaps.toString(),
            color = if (isSystemInDarkTheme())
                MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary,
            fontSize = 36.sp
        )
    }
}


