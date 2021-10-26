package com.projects.oliver_graham.earquizmvp.data

object QuestionsRepo {
    fun getRadioLabelsMap(): Map<String, String> = intervalLabels
}


private val intervalLabels: Map<String, String> = mapOf(
    "m2" to "Minor 2nd (half step)",
    "M2" to "Major 2nd (whole step)",
    "m3" to "Minor 3rd",
    "M3" to "Major 3rd",
    "P4" to "Perfect 4th",
    "a4" to "Tritone",
    "P5" to "Perfect 5th",
    "m6" to "Minor 6th",
    "M6" to "Major 6th",
    "m7" to "Minor 7th",
    "M7" to "Major 7th",
    "P8" to "Octave",
)

