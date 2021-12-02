package com.projects.oliver_graham.earquizmvp.data

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class Note(
    val id: Int,
    val name: String,
    val accidental: Int,         // -1 == flat, 0 == natural, 1 == sharp
    val soundPath: String,
    @DrawableRes val imageId: Int
) {

}