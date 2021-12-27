package com.projects.oliver_graham.earquizmvp.data

import com.projects.oliver_graham.earquizmvp.R

object QuestionsRepo {
    fun getIntervalsByHalfStep(): Map<Int, String> = intervalsByHalfStep
    fun getNotes(): List<Note> = notes
    fun getAllQuizDescriptions(): List<QuizDescription> = allDescriptions
    // TODO get individual quiz descriptions
    fun getMelodicQuizDescriptions(): List<String> = melodicIntervalQuizDescription
    fun getHarmonicQuizDescriptions(): List<String> = harmonicIntervalQuizDescription
}

private val intervalsByHalfStep: Map<Int, String> = mapOf(
    1 to "Minor 2nd (half step)",
    2 to "Major 2nd (whole step)",
    3 to "Minor 3rd",
    4 to "Major 3rd",
    5 to "Perfect 4th",
    6 to "Tritone",
    7 to "Perfect 5th",
    8 to "Minor 6th",
    9 to "Major 6th",
    10 to "Minor 7th",
    11 to "Major 7th",
    12 to "Octave",
)

private val notes: List<Note> = listOf(
    Note(pitch = 40, name = "c3", accidental = 0, soundPath = "C3", imageId = R.drawable.c3),
    Note(pitch = 41, name = "c_sharp3", accidental = 1, soundPath = "C%233", imageId = R.drawable.c_sharp3),
    Note(pitch = 41, name = "d_flat3", accidental = -1, soundPath = "C%233", imageId = R.drawable.d_flat3),
    Note(pitch = 42, name = "d3", accidental = 0, soundPath = "D3", imageId = R.drawable.d3),
    Note(pitch = 43, name = "d_sharp3", accidental = 1, soundPath = "D%233", imageId = R.drawable.d_sharp3),
    Note(pitch = 43, name = "e_flat3", accidental = -1, soundPath = "D%233", imageId = R.drawable.e_flat3),
    Note(pitch = 44, name = "e3", accidental = 0, soundPath = "E3", imageId = R.drawable.e3),
    Note(pitch = 45, name = "f3", accidental = 0, soundPath = "F3", imageId = R.drawable.f3),
    Note(pitch = 46, name = "f_sharp3", accidental = 1, soundPath = "F%233", imageId = R.drawable.f_sharp3),
    Note(pitch = 46, name = "g_flat3", accidental = -1, soundPath = "F%233", imageId = R.drawable.g_flat3),
    Note(pitch = 47, name = "g3", accidental = 0, soundPath = "G3", imageId = R.drawable.g3),
    Note(pitch = 48, name = "g_sharp3", accidental = 1, soundPath = "G%233", imageId = R.drawable.g_sharp3),
    Note(pitch = 48, name = "a_flat3", accidental = -1, soundPath = "G%233", imageId = R.drawable.a_flat3),
    Note(pitch = 49, name = "a3", accidental = 0, soundPath = "A3", imageId = R.drawable.a3),
    Note(pitch = 50, name = "a_sharp3", accidental = 1, soundPath = "A%233", imageId = R.drawable.a_sharp3),
    Note(pitch = 50, name = "b_flat3", accidental = -1, soundPath = "A%233", imageId = R.drawable.b_flat3),
    Note(pitch = 51, name = "b3", accidental = 0, soundPath = "B3", imageId = R.drawable.b3),

    Note(pitch = 52, name = "c4", accidental = 0, soundPath = "C4", imageId = R.drawable.c4),
    Note(pitch = 53, name = "c_sharp4", accidental = 1, soundPath = "C%234", imageId = R.drawable.c_sharp4),
    Note(pitch = 53, name = "d_flat4", accidental = -1, soundPath = "C%234", imageId = R.drawable.d_flat4),
    Note(pitch = 54, name = "d4", accidental = 0, soundPath = "D4", imageId = R.drawable.d4),
    Note(pitch = 55, name = "d_sharp4", accidental = 1, soundPath = "D%234", imageId = R.drawable.d_sharp4),
    Note(pitch = 55, name = "e_flat4", accidental = -1, soundPath = "D%234", imageId = R.drawable.e_flat4),
    Note(pitch = 56, name = "e4", accidental = 0, soundPath = "E4", imageId = R.drawable.e4),
    Note(pitch = 57, name = "f4", accidental = 0, soundPath = "F4", imageId = R.drawable.f4),
    Note(pitch = 58, name = "f_sharp4", accidental = 1, soundPath = "F%234", imageId = R.drawable.f_sharp4),
    Note(pitch = 58, name = "g_flat4", accidental = -1, soundPath = "F%234", imageId = R.drawable.g_flat4),
    Note(pitch = 59, name = "g4", accidental = 0, soundPath = "G4", imageId = R.drawable.g4),
    Note(pitch = 60, name = "g_sharp4", accidental = 1, soundPath = "G%234", imageId = R.drawable.g_sharp4),
    Note(pitch = 60, name = "a_flat4", accidental = -1, soundPath = "G%234", imageId = R.drawable.a_flat4),
    Note(pitch = 61, name = "a4", accidental = 0, soundPath = "A4", imageId = R.drawable.a4),
    Note(pitch = 62, name = "a_sharp4", accidental = 1, soundPath = "A%234", imageId = R.drawable.a_sharp4),
    Note(pitch = 62, name = "b_flat4", accidental = -1, soundPath = "A%234", imageId = R.drawable.b_flat4),
    Note(pitch = 63, name = "b4", accidental = 0, soundPath = "B4", imageId = R.drawable.b4),

/*    Note(pitch = 64, name = "c5", accidental = 0, soundPath = "C5", imageId = 0),              // C
    Note(pitch = 65, name = "c_sharp5", accidental = 1, soundPath = "C%235", imageId = 0),     // C#
    Note(pitch = 65, name = "d_flat5", accidental = -1, soundPath = "C%235", imageId = 0),     // Db
    Note(pitch = 66, name = "d5", accidental = 0, soundPath = "D5", imageId = 0),              // D
    Note(pitch = 67, name = "d_sharp5", accidental = 1, soundPath = "D%235", imageId = 0),     // D#
    Note(pitch = 67, name = "e_flat5", accidental = -1, soundPath = "D%235", imageId = 0),     // Eb
    Note(pitch = 68, name = "e5", accidental = 0, soundPath = "E5", imageId = 0),              // E
    Note(pitch = 69, name = "f5", accidental = 0, soundPath = "F5", imageId = 0),              // F
    Note(pitch = 70, name = "f_sharp5", accidental = 1, soundPath = "F%235", imageId = 0),     // F#
    Note(pitch = 70, name = "g_flat5", accidental = -1, soundPath = "F%235", imageId = 0),     // Gb
    Note(pitch = 71, name = "g5", accidental = 0, soundPath = "G5", imageId = 0),              // G
    Note(pitch = 72, name = "g_sharp5", accidental = 1, soundPath = "G%235", imageId = 0),     // G#
    Note(pitch = 72, name = "a_flat5", accidental = -1, soundPath = "G%235", imageId = 0),     // Ab
    Note(pitch = 73, name = "a5", accidental = 0, soundPath = "A5", imageId = 0),              // A
    Note(pitch = 74, name = "a_sharp5", accidental = 1, soundPath = "A%235", imageId = 0),     // A#
    Note(pitch = 74, name = "b_flat5", accidental = -1, soundPath = "A%235", imageId = 0),     // Bb
    Note(pitch = 75, name = "b5", accidental = 0, soundPath = "B5", imageId = 0),              // B*/
)

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

private val randomQuizDescription: List<String> = listOf(
    "Quiz coming soon!",
    "Will contain a mixture of intervals and chords"
)

private val allDescriptions: List<QuizDescription> = listOf(
    QuizDescription(
        title = "Melodic Intervals",
        descriptions = melodicIntervalQuizDescription
    ),
    QuizDescription(
        title = "Harmonic Intervals",
        descriptions = harmonicIntervalQuizDescription
    ),
    QuizDescription(
        title = "Easy Chords",
        descriptions = easyChordQuizDescription
    ),
    QuizDescription(
        title = "Medium Chords",
        descriptions = mediumChordQuizDescription
    ),
    QuizDescription(
        title = "Random Quiz",
        descriptions = randomQuizDescription
    )
)
