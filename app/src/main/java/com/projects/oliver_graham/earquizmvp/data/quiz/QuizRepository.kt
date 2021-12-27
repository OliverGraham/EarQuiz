package com.projects.oliver_graham.earquizmvp.data.quiz

object QuizRepository {
    fun getMelodicQuizDescriptions(): List<String> = melodicIntervalQuizDescription
    fun getHarmonicQuizDescriptions(): List<String> = harmonicIntervalQuizDescription
    fun getRandomIntervalsQuizDescription(): List<String> = randomIntervalsQuizDescription
    fun getEasyChordQuizDescription(): List<String> = easyChordQuizDescription
    fun getMediumChordQuizDescription(): List<String> = mediumChordQuizDescription
    fun getRandomMixQuizDescription(): List<String> = randomMixQuizDescription

}

// HomeScreen Data
private val melodicIntervalQuizDescription: List<String> = listOf(
    "Try to guess the interval",
    "Intervals will be ascending or descending, within an octave",
    "There will be four possible choices; one correct",
    "Try to guess in as few listens as possible",
    "If you get stumped hints may appear"
)

private val harmonicIntervalQuizDescription: List<String> = listOf(
    "Two notes will be played at the same time",
    "There will be four possible choices; one correct",
    "Try to guess in as few listens as possible"
)

private val randomIntervalsQuizDescription: List<String> = listOf(
    "Random mix of melodic and harmonic intervals",
    "Answer format is multiple choice"
)

private val easyChordQuizDescription: List<String> = listOf(
    "Quiz coming soon!",
    "Will contain triads of major and minor quality only",
    "Chords could be inverted or in root position"
)

private val mediumChordQuizDescription: List<String> = listOf(
    "Quiz coming soon!",
    "Chords could contain 7ths",
    "All types of chord qualities (half-diminished, minor-major, etc)",
    "Chords could be inverted or in root position"
)

private val randomMixQuizDescription: List<String> = listOf(
    "Quiz coming soon!",
    "Will contain a mixture of intervals and chords"
)