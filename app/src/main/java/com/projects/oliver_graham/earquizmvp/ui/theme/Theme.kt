package com.projects.oliver_graham.earquizmvp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = BlueGray600,
    primaryVariant = LightBlueA100,
    secondary = Teal200,
    background = Gray800,
    onPrimary = LightBlueA100,
    surface = Gray800
)

private val LightColorPalette = lightColors(
    primary = BlueGray600,           // button color, for instance
    primaryVariant = LightBlueA100,
    secondary = Teal200,
    background = Gray300,
    onPrimary = LightBlue300
    /* Other default colors to override
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun EarQuizMVPTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}