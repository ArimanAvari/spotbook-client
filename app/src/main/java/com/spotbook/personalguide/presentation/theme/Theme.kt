package com.spotbook.personalguide.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF1E4D3B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCECE2),
    onPrimaryContainer = Color(0xFF123426),
    secondary = Color(0xFF4F7A5A),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDCECE2),
    onSecondaryContainer = Color(0xFF173A27),
    tertiary = Color(0xFFA8C5B1),
    onTertiary = Color(0xFF173A27),
    tertiaryContainer = Color(0xFFEAF3ED),
    onTertiaryContainer = Color(0xFF173A27),
    background = Color(0xFFF6FAF7),
    onBackground = Color(0xFF17211B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF17211B),
    surfaceVariant = Color(0xFFEAF3ED),
    onSurfaceVariant = Color(0xFF405149),
    outline = Color(0xFFA8C5B1),
    outlineVariant = Color(0xFFDCECE2),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA8C5B1),
    onPrimary = Color(0xFF0F2F22),
    primaryContainer = Color(0xFF285B45),
    onPrimaryContainer = Color(0xFFDCECE2),
    secondary = Color(0xFFB3CCB9),
    onSecondary = Color(0xFF20382A),
    secondaryContainer = Color(0xFF36503D),
    onSecondaryContainer = Color(0xFFCFE8D5),
    tertiary = Color(0xFFBCD8C3),
    onTertiary = Color(0xFF21382A),
    background = Color(0xFF101713),
    onBackground = Color(0xFFE0E9E2),
    surface = Color(0xFF151D18),
    onSurface = Color(0xFFE0E9E2),
    surfaceVariant = Color(0xFF28352D),
    onSurfaceVariant = Color(0xFFC0CCC4),
    outline = Color(0xFF89968E)
)

private val SpotBookShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

private val SpotBookTypography = Typography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)

@Composable
fun SpotBookTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        shapes = SpotBookShapes,
        typography = SpotBookTypography,
        content = content
    )
}
