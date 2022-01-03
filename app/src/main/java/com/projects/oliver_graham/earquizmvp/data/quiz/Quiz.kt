package com.projects.oliver_graham.earquizmvp.data.quiz

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.projects.oliver_graham.earquizmvp.data.QuizQuestion

sealed class Quiz private constructor(
    val quizIndex: Int = 0,
    val title: String = "",
    val descriptions: List<String> = listOf(),
    val isEnabled: Boolean = false,
    var isInProgress: Boolean = false,
    var totalQuestions: Int = 5,
    var currentQuestion: QuizQuestion = QuizQuestion()
) {

    // TODO this will be only place that can access the repo. That should be good?
    private object MelodicIntervalQuiz : Quiz(
        quizIndex = 0,
        title = "Melodic Intervals",
        descriptions = QuizRepository.getMelodicQuizDescriptions(),
        isEnabled = true
    )
    private object HarmonicIntervalQuiz : Quiz(
        quizIndex = 1,
        title = "Harmonic Intervals",
        descriptions = QuizRepository.getHarmonicQuizDescriptions(),
        isEnabled = true
    )
    private object RandomIntervalsQuiz : Quiz(
        quizIndex = 2,
        title = "Random Interval Mix",
        descriptions = QuizRepository.getRandomIntervalsQuizDescription(),
        isEnabled = true
    )
    private object EasyChordQuiz : Quiz(
        quizIndex = 3,
        title = "Easy Chords",
        descriptions = QuizRepository.getEasyChordQuizDescription(),
        isEnabled = false
    )
    private object MediumChordQuiz : Quiz(
        quizIndex = 4,
        title = "Medium Chords",
        descriptions = QuizRepository.getMediumChordQuizDescription(),
        isEnabled = false
    )
    private object RandomMixQuiz : Quiz(
        quizIndex = 5,
        title = "Random Interval/Chord Mix",
        descriptions = QuizRepository.getRandomMixQuizDescription(),
        isEnabled = false
    )
    private object FakeQuiz : Quiz()  // this serves as the initial value for the mutable state
                                        // is there another way? Couldn't find one

    companion object {
        private val quizInProgress: MutableState<Quiz> = mutableStateOf(value = FakeQuiz)

        fun getQuizInProgress(): Quiz = quizInProgress.value

        fun setQuizInProgress(quiz: Quiz) {
            stopCurrentQuiz()   // set false regardless?
            quizInProgress.value = quiz
            startCurrentQuiz()
        }

        fun startCurrentQuiz() { quizInProgress.value.isInProgress = true }
        fun stopCurrentQuiz() { quizInProgress.value.isInProgress = false }

        fun isThereAQuizInProgress(): Boolean {
            getAllQuizzes().forEach { quiz ->
                if (quiz.isInProgress)
                    return true
            }
            return false
        }

        fun getAllQuizzes(): List<Quiz> = listOf(
            MelodicIntervalQuiz,
            HarmonicIntervalQuiz,
            RandomIntervalsQuiz,
            EasyChordQuiz,
            MediumChordQuiz,
            RandomMixQuiz
        )

        /*private val quizMap: Map<Int, Quiz> = mapOf(
            0 to MelodicIntervalQuiz,
            1 to HarmonicIntervalQuiz,
            2 to RandomIntervalsQuiz
        )*/

    }

}

