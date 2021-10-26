package com.projects.oliver_graham.earquizmvp.data

import androidx.compose.runtime.Immutable

@Immutable
data class Note(
    val id: Int         // math to cehck if further away than an octave, get new note othersiwe
) {

}