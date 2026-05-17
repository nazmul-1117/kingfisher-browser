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
    LinearProgressIndicator(
        progress = { if (isLoading) progress / 100f else 0f },
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp),
        color = AccentCyan,
        trackColor = Color.Transparent
    )
}