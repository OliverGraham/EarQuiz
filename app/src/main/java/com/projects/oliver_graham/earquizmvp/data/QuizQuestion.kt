package com.projects.oliver_graham.earquizmvp.data

import android.media.SoundPool
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class QuizQuestion(
    val id: String,
    val text: String,
   // @DrawableRes val imageId: Int,      // put image in res folder, then --> asset = imageResource(post.imageId),
    val soundFilePath: String
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
