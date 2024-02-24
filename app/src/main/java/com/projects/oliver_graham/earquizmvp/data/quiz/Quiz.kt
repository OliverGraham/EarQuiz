package com.projects.oliver_graham.earquizmvp.data.quiz

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.projects.oliver_graham.earquizmvp.data.musictheory.MusicTheory
import com.projects.oliver_graham.earquizmvp.data.musictheory.Note

// TODO: Redo this, it needs it badly (what was I thinking?)

sealed class Quiz private constructor(
    val quizIndex: Int = 0,
    val title: String = "",
    val quizScreenTitle: String = "",
    val descriptions: List<String> = listOf(),
    val isEnabled: Boolean = false,
    val questionList: MutableList<QuizQuestion> = mutableListOf(),
    var isInProgress: Boolean = false,
    var totalQuestions: Int = 3,
) {
    object MelodicIntervalQuiz : Quiz(
        quizIndex = 0,
        title = "Melodic Intervals",
        quizScreenTitle = "Melodic\nIntervals",
        descriptions = QuizRepository.getMelodicQuizDescriptions(),
        isEnabled = true
    )
    object HarmonicIntervalQuiz : Quiz(
        quizIndex = 1,
        title = "Harmonic Intervals",
        quizScreenTitle = "Harmonic\nIntervals",
        descriptions = QuizRepository.getHarmonicQuizDescriptions(),
        isEnabled = true
    )
    object RandomIntervalsQuiz : Quiz(
        quizIndex = 2,
        title = "Random Interval Mix",
        descriptions = QuizRepository.getRandomIntervalsQuizDescription(),
        isEnabled = true
    )
    object EasyChordQuiz : Quiz(
        quizIndex = 3,
        title = "Easy Chords",
        descriptions = QuizRepository.getEasyChordQuizDescription(),
        isEnabled = false
    )
    object MediumChordQuiz : Quiz(
        quizIndex = 4,
        title = "Medium Chords",
        descriptions = QuizRepository.getMediumChordQuizDescription(),
        isEnabled = false
    )
    object RandomMixQuiz : Quiz(
        quizIndex = 5,
        title = "Random Interval/Chord Mix",
        descriptions = QuizRepository.getRandomMixQuizDescription(),
        isEnabled = false
    )
    object FakeQuiz : Quiz()  // this serves as the initial value for the mutable state
                                      // is there another way? Couldn't find one

    companion object {
        private val quizInProgress: MutableState<Quiz> = mutableStateOf(value = FakeQuiz)



        fun setQuizInProgress(quiz: Quiz, numberOfQuestions: Int) {
            quizInProgress.value = quiz
            quizInProgress.value.isInProgress = true
            quiz.totalQuestions = numberOfQuestions
            createQuizQuestions(numberOfQuestions = numberOfQuestions)
        }

        fun getQuizInProgress(): Quiz = quizInProgress.value

        fun isQuizInProgress(): Boolean = quizInProgress.value.isInProgress

        private fun createQuizQuestions(numberOfQuestions: Int) {
            val quiz = quizInProgress.value
            if (!quiz.isInProgress) return

            when (quiz) {
                RandomIntervalsQuiz,
                MelodicIntervalQuiz,
                HarmonicIntervalQuiz -> createTwoNoteQuiz(numberOfQuestions)
                EasyChordQuiz -> TODO()
                MediumChordQuiz -> TODO()
                RandomMixQuiz -> TODO()
                FakeQuiz -> TODO()
            }
        }

        private fun createTwoNoteQuiz(numberOfQuestions: Int) {
            for (i in 0..numberOfQuestions) {
                // three random intervals and one correct
                val twoCurrentNotes: List<Note> = MusicTheory.createTwoRandomNotes()
                val noteOne = twoCurrentNotes[0]
                val noteTwo = twoCurrentNotes[1]
                val keyInterval: Int = kotlin.math.abs(n = noteOne.pitch - noteTwo.pitch)
                val randomIntervals: List<Int> = MusicTheory.getRandomIntervals(keyInterval)

                quizInProgress.value.questionList.add(
                    QuizQuestion(
                        correctId = keyInterval,
                        correctText = MusicTheory.getIntervalLabel(keyInterval),
                        allAnswers = randomIntervals,
                        soundIds = listOf(noteOne.pitch, noteTwo.pitch),
                        firstNote = noteOne.imageId,
                        secondNote = noteTwo.imageId
                    )
                )
            }
        }

        private fun createThreeNoteQuiz(numberOfQuestions: Int) {
            for (i in 0..numberOfQuestions) {
                // TODO
            }
        }

        fun getCurrentQuestion(): QuizQuestion {
            return quizInProgress.value.questionList[quizInProgress.value.questionList.lastIndex]
        }

        fun removeAskedQuestion() {
            quizInProgress.value.questionList.removeAt(quizInProgress.value.questionList.lastIndex)
        }

        fun getCurrentQuestionLabels(): MutableList<Pair<Int, String>> {

            val pairList: MutableList<Pair<Int, String>> = mutableListOf()

            getCurrentQuestion().allAnswers.forEach { interval ->
                pairList.add(Pair(interval, MusicTheory.getIntervalLabel(interval)))
            }

            return pairList
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

