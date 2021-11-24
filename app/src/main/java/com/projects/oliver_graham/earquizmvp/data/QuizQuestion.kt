package com.projects.oliver_graham.earquizmvp.data

import android.media.SoundPool
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.projects.oliver_graham.earquizmvp.R

@Immutable
data class QuizQuestion(
    val id: Int,
    val text: String,
    @DrawableRes val clefsImage: Int = 0,   // only correct answer needs reference to images
    @DrawableRes val firstNote: Int = 0,
    @DrawableRes val secondNote: Int = 0
    )

@Immutable
data class RadioQuizInfo(
    val id: String,
    val text: String
)


/*
Image(
asset = imageResource(post.imageId),
contentScale = ContentScale.Crop,
modifier = Modifier
.preferredHeightIn(min = 180.dp)
.fillMaxWidth()
)*/
