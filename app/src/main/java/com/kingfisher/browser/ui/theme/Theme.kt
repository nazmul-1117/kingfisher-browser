package com.kingfisher.browser.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Brand colors
val BackgroundDark = Color(0xFF0B0F1A)
val SurfaceDark = Color(0xFF010B31)
val AccentCyan = Color(0xFF00E5FF)
val AccentPurple = Color(0xFF8E24AA)

val LightColorScheme = lightColorScheme(
    background = Color.White,
    surface = Color.White,
    surfaceVariant = Color(0xFFF1F3F4),

    primary = AccentCyan,
    onPrimary = Color.Black,

    secondary = AccentPurple,
    onSecondary = Color.White,

    onBackground = Color.Black,
    onSurface = Color.Black,
    onSurfaceVariant = Color(0xFF666666)
)

val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = Color(0xFF1A223D),

    primary = AccentCyan,
    onPrimary = Color.Black,

    secondary = AccentPurple,
    onSecondary = Color.White,

    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFB0B3C0)
)

@Composable
fun KingfisherTheme(
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (isSystemInDarkTheme()) DarkColorScheme
        else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}