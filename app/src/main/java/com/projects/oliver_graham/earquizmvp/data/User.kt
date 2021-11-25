package com.projects.oliver_graham.earquizmvp.data

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val uid: String,
    val userName: String,
    val email: String,
    var correctAnswers: Int = 0,
    var incorrectAnswers: Int = 0
) {
    fun totalQuestionsAttempted() = correctAnswers + incorrectAnswers
}