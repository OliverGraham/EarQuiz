package com.projects.oliver_graham.earquizmvp.data.quiz

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.projects.oliver_graham.earquizmvp.R

@Immutable
data class QuizQuestion(
    val correctId: Int = 0,
    val correctText: String = "",
    val allAnswers: List<Int> = listOf(),    // int is identifier for radio group, etc
    val soundIds: List<Int> = listOf(),      // id for SoundPLayer
    @DrawableRes val clefsImage: Int = R.drawable.treble_and_bass_clef,
    @DrawableRes val firstNote: Int = 0,
    @DrawableRes val secondNote: Int = 0
)

