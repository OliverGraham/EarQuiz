package com.projects.oliver_graham.earquizmvp.data

import androidx.compose.runtime.Immutable

@Immutable
data class QuizDescription(
    val title: String,
    val descriptions: List<String>
)