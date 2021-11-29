package com.projects.oliver_graham.earquizmvp.data

import androidx.compose.runtime.Immutable
import com.google.firebase.firestore.DocumentId

@Immutable
data class User(
    @DocumentId val uid: String = "",
    val userName: String = "",
    val email: String = "",
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0
) {
    fun totalQuestionsAttempted() = correctAnswers + incorrectAnswers
}