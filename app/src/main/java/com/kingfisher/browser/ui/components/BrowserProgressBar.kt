package com.kingfisher.browser.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kingfisher.browser.ui.theme.AccentCyan

@Composable
fun BrowserProgressBar(
    isLoading: Boolean,
    progress: Int
) {
    val safeProgress = (progress / 100f).coerceIn(0f, 1f)

    if (isLoading) {
        LinearProgressIndicator(
            progress = { safeProgress },   // ✅ NEW REQUIRED LAMBDA FORM
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp),
            color = AccentCyan,
            trackColor = Color.Transparent
        )
    }
}