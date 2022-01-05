package com.projects.oliver_graham.earquizmvp.data.quiz

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class QuizQuestion(
    val id: Int = 0,
    val text: String = "",
    @DrawableRes val clefsImage: Int = 0,   // only correct answer needs reference to images
    @DrawableRes val firstNote: Int = 0,
    @DrawableRes val secondNote: Int = 0
)
