package com.projects.oliver_graham.earquizmvp.data.musictheory

import com.projects.oliver_graham.earquizmvp.R
import kotlin.random.Random

object MusicTheory {

    fun getIntervalLabel(halfStep: Int): String = intervalLabelsByHalfStep.getValue(key = halfStep)

    // returns a list of two random notes that make up an interval
    fun createTwoRandomNotes(): List<Note> {

        val size = notes.size
        val randomNoteOne = notes[Random.nextInt(size)]
        val randomNoteTwo = notes[Random.nextInt(size)]

        if (accidentalPreference(randomNoteOne.accidental, randomNoteTwo.accidental))
            if (kotlin.math.abs(n = randomNoteOne.pitch - randomNoteTwo.pitch) in 1..12)
                return listOf(randomNoteOne, randomNoteTwo)

        return createTwoRandomNotes()
    }

    // returns random intervals, excluding the passed keyInterval
    fun getRandomIntervals(keyInterval: Int, randomListSize: Int = 4): List<Int> {

        val randomIntervalList = mutableListOf(keyInterval)

        while (randomIntervalList.size < randomListSize) {
            val currentRandom = (1..12).random()

            if (!randomIntervalList.contains(currentRandom))
                randomIntervalList.add(currentRandom)
        }
        return randomIntervalList
    }

    // avoids mixing sharps and flats for the images
    private fun accidentalPreference(accidental1: Int, accidental2: Int): Boolean {

        // natural can mix with either/or (sometimes not ideal - might make better later)
        if (accidental1 == 0 || accidental2 == 0)
            return true

        if (accidental1 == -1 && accidental2 == -1)
            return true

        if (accidental1 == 1 && accidental2 == 1)
            return true

        return false
    }

    private val intervalLabelsByHalfStep: Map<Int, String> = mapOf(
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
        Note(pitch = 40, name = "c3", accidental = 0, imageId = R.drawable.c3),
        Note(pitch = 41, name = "c_sharp3", accidental = 1, imageId = R.drawable.c_sharp3),
        Note(pitch = 41, name = "d_flat3", accidental = -1, imageId = R.drawable.d_flat3),
        Note(pitch = 42, name = "d3", accidental = 0, imageId = R.drawable.d3),
        Note(pitch = 43, name = "d_sharp3", accidental = 1, imageId = R.drawable.d_sharp3),
        Note(pitch = 43, name = "e_flat3", accidental = -1, imageId = R.drawable.e_flat3),
        Note(pitch = 44, name = "e3", accidental = 0, imageId = R.drawable.e3),
        Note(pitch = 45, name = "f3", accidental = 0, imageId = R.drawable.f3),
        Note(pitch = 46, name = "f_sharp3", accidental = 1, imageId = R.drawable.f_sharp3),
        Note(pitch = 46, name = "g_flat3", accidental = -1, imageId = R.drawable.g_flat3),
        Note(pitch = 47, name = "g3", accidental = 0, imageId = R.drawable.g3),
        Note(pitch = 48, name = "g_sharp3", accidental = 1, imageId = R.drawable.g_sharp3),
        Note(pitch = 48, name = "a_flat3", accidental = -1, imageId = R.drawable.a_flat3),
        Note(pitch = 49, name = "a3", accidental = 0, imageId = R.drawable.a3),
        Note(pitch = 50, name = "a_sharp3", accidental = 1, imageId = R.drawable.a_sharp3),
        Note(pitch = 50, name = "b_flat3", accidental = -1, imageId = R.drawable.b_flat3),
        Note(pitch = 51, name = "b3", accidental = 0, imageId = R.drawable.b3),

        Note(pitch = 52, name = "c4", accidental = 0, imageId = R.drawable.c4),
        Note(pitch = 53, name = "c_sharp4", accidental = 1, imageId = R.drawable.c_sharp4),
        Note(pitch = 53, name = "d_flat4", accidental = -1, imageId = R.drawable.d_flat4),
        Note(pitch = 54, name = "d4", accidental = 0, imageId = R.drawable.d4),
        Note(pitch = 55, name = "d_sharp4", accidental = 1, imageId = R.drawable.d_sharp4),
        Note(pitch = 55, name = "e_flat4", accidental = -1, imageId = R.drawable.e_flat4),
        Note(pitch = 56, name = "e4", accidental = 0, imageId = R.drawable.e4),
        Note(pitch = 57, name = "f4", accidental = 0, imageId = R.drawable.f4),
        Note(pitch = 58, name = "f_sharp4", accidental = 1, imageId = R.drawable.f_sharp4),
        Note(pitch = 58, name = "g_flat4", accidental = -1, imageId = R.drawable.g_flat4),
        Note(pitch = 59, name = "g4", accidental = 0, imageId = R.drawable.g4),
        Note(pitch = 60, name = "g_sharp4", accidental = 1, imageId = R.drawable.g_sharp4),
        Note(pitch = 60, name = "a_flat4", accidental = -1, imageId = R.drawable.a_flat4),
        Note(pitch = 61, name = "a4", accidental = 0, imageId = R.drawable.a4),
        Note(pitch = 62, name = "a_sharp4", accidental = 1, imageId = R.drawable.a_sharp4),
        Note(pitch = 62, name = "b_flat4", accidental = -1, imageId = R.drawable.b_flat4),
        Note(pitch = 63, name = "b4", accidental = 0, imageId = R.drawable.b4),

        /*Note(pitch = 64, name = "c5", accidental = 0, imageId = 0),              // C
        Note(pitch = 65, name = "c_sharp5", accidental = 1, imageId = 0),     // C#
        Note(pitch = 65, name = "d_flat5", accidental = -1, imageId = 0),     // Db
        Note(pitch = 66, name = "d5", accidental = 0, imageId = 0),              // D
        Note(pitch = 67, name = "d_sharp5", accidental = 1, imageId = 0),     // D#
        Note(pitch = 67, name = "e_flat5", accidental = -1, imageId = 0),     // Eb
        Note(pitch = 68, name = "e5", accidental = 0, imageId = 0),              // E
        Note(pitch = 69, name = "f5", accidental = 0, imageId = 0),              // F
        Note(pitch = 70, name = "f_sharp5", accidental = 1, imageId = 0),     // F#
        Note(pitch = 70, name = "g_flat5", accidental = -1, imageId = 0),     // Gb
        Note(pitch = 71, name = "g5", accidental = 0, imageId = 0),              // G
        Note(pitch = 72, name = "g_sharp5", accidental = 1, imageId = 0),     // G#
        Note(pitch = 72, name = "a_flat5", accidental = -1, imageId = 0),     // Ab
        Note(pitch = 73, name = "a5", accidental = 0, imageId = 0),              // A
        Note(pitch = 74, name = "a_sharp5", accidental = 1, imageId = 0),     // A#
        Note(pitch = 74, name = "b_flat5", accidental = -1, imageId = 0),     // Bb
        Note(pitch = 75, name = "b5", accidental = 0, imageId = 0),              // B*/
    )
}
