package com.projects.oliver_graham.earquizmvp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.projects.oliver_graham.earquizmvp.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.architects_daughter_regular)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    // Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily(Font(R.font.architects_daughter_regular)),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily(Font(R.font.architects_daughter_regular)),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )

)

