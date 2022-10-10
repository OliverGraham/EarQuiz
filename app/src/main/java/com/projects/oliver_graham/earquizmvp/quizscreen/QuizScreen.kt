package com.projects.oliver_graham.earquizmvp.quizscreen


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projects.oliver_graham.earquizmvp.data.quiz.QuizQuestion
import com.projects.oliver_graham.earquizmvp.ui.*
import kotlinx.coroutines.Job


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun QuizScreen(viewModel: QuizScreenViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) { ->
        TopRow(
            quizName = viewModel.getQuizName(),
            questionNumber = viewModel.questionNumber.value,
            totalQuestions = viewModel.currentQuiz.totalQuestions,
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
            for (labelPair in viewModel.radioGroup) {
                TextAndRadio(
                    text = labelPair.second,
                    radioID = labelPair.first,
                    radioChoice = viewModel.currentUserChoice,
                    enableOtherUI = viewModel.submitButtonEnabled
                )
            }
        }

        CenteredContentRow { ->
            ThemeButton(
                text = "Submit Answer",
                onButtonClick = { viewModel.showAnswerDialog.value = !viewModel.showAnswerDialog.value },
                mutableEnabled = viewModel.submitButtonEnabled,
                innerContent = { Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = null) }
            )
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
                currentQuestion = viewModel.currentQuestion
            )
        }

        if (viewModel.showFinishedDialog.value) {
            FinishedDialog(
                dismissRequest = {
                    viewModel.resetQuizScreen()
                    viewModel.navToHomeScreen()
                },
                title = "Finished! ${viewModel.questionNumber.value}/${viewModel.currentQuiz.totalQuestions}",
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
                }
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
    currentQuestion: QuizQuestion
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
        correctAnswerText = currentQuestion.correctText,
        incorrectAnswerText = userChoiceText,
        playSound = { playSound(false) },
        playButtonEnabled = playButtonEnabled,
        nextButtonEnabled = nextButtonEnabled,
        clefsResourceId = currentQuestion.clefsImage,
        noteOneResourceId = currentQuestion.firstNote,
        noteTwoResourceId = currentQuestion.secondNote,
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
                    tint = Color(color = 0xFF689F38)
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) { ->
                    Image(
                        painter = painterResource(id = clefsResourceId),
                        contentDescription = null,
                        contentScale = ContentScale.None
                    )
                    Image(
                        painter = painterResource(id = noteOneResourceId),
                        contentDescription = null,
                        contentScale = ContentScale.None
                    )
                    Image(
                        painter = painterResource(id = noteTwoResourceId),
                        contentDescription = null,
                        contentScale = ContentScale.None
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
        },
        backgroundColor = MaterialTheme.colors.background
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
    onLeaderboardButtonClick: () -> Unit
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
                    content = { ->
                        LargeText(
                            text = "Leaderboard",
                            fontSize = 12.sp
                        )
                        Icon(Icons.Rounded.Notifications, contentDescription = "")
                    }
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background
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
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(4.dp))

            LargeText(
                text = "+ $correctUserAnswers",
                fontSize = 32.sp,
                color = Color(color = 0xFF689F38)   // regular green too bright
            )
            Spacer(modifier = Modifier.padding(4.dp))

            LargeText(
                text = "- $incorrectUserAnswers",
                fontSize = 32.sp,
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
        ThemeButton(
            text = "Play Interval",
            onButtonClick = { playSound() },
            mutableEnabled = playButtonEnabled,
            innerContent = { Icon(Icons.Rounded.PlayArrow, contentDescription = "") }
        )
        LargeText(
            text = numberOfIntervalTaps.toString(),
            color = if (isSystemInDarkTheme())
                MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.secondaryVariant,
            fontSize = 36.sp
        )
    }
}


