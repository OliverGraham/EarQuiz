package com.projects.oliver_graham.earquizmvp.data.quiz

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.projects.oliver_graham.earquizmvp.R
import com.projects.oliver_graham.earquizmvp.data.musictheory.Note

sealed class Quiz private constructor(
    val quizIndex: Int = 0,
    val title: String = "",
    val descriptions: List<String> = listOf(),
    val isEnabled: Boolean = false,             // if quiz is supported yet
    var isInProgress: Boolean = false,
    var totalQuestions: Int = 1,
    var currentQuestion: QuizQuestion = QuizQuestion()
) {
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
        isEnabled = false
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

    // public static api for all things quiz related
    companion object {
        private val quizInProgress: MutableState<Quiz> = mutableStateOf(value = FakeQuiz)

        fun setQuizInProgress(quiz: Quiz) {
            stopCurrentQuiz()           // set false regardless
            quizInProgress.value = quiz
            startCurrentQuiz()
        }

        fun getQuizInProgress(): Quiz = quizInProgress.value

        fun setCurrentQuestion(id: Int, text: String, firstNote: Note, secondNote: Note) {
            quizInProgress.value.currentQuestion = QuizQuestion(
                id = id,
                text = text,
                clefsImage = R.drawable.treble_and_bass_clef,
                firstNote = firstNote.imageId,
                secondNote = secondNote.imageId
            )
        }
        fun getCurrentQuestion(): QuizQuestion = quizInProgress.value.currentQuestion

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

