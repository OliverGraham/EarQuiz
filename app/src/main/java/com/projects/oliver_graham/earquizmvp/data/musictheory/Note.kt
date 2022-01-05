package com.projects.oliver_graham.earquizmvp.data.musictheory

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class Note(
    val pitch: Int,
    val name: String,
    val accidental: Int,            // -1 == flat, 0 == natural, 1 == sharp
    @DrawableRes val imageId: Int
)