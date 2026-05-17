package com.kingfisher.browser.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Exact colors requested
val BackgroundDark = Color(0xFF0B0F1A)
val SurfaceDark = Color(0xFF141A2E)
val AccentCyan = Color(0xFF00E5FF)
val AccentPurple = Color(0xFF8E24AA)

val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    surface = SurfaceDark,
    primary = AccentCyan,
    secondary = AccentPurple,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun KingfisherTheme(
    content: @Composable () -> Unit
) {
    // We removed the old SideEffect window logic entirely from here!

    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}