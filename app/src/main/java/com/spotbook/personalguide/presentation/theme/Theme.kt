package com.spotbook.personalguide.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF315D72),
    secondary = Color(0xFF6B5E3F),
    tertiary = Color(0xFF78607B),
    background = Color(0xFFF8FAF9),
    surface = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9CCBE0),
    secondary = Color(0xFFD8C89A),
    tertiary = Color(0xFFE2BDE4)
)

@Composable
fun SpotBookTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}

