package com.kingfisher.browser.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue

@Composable
fun LoadingProgressBar(progress: Float) {

    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(250),
        label = "progress"
    )

    LinearProgressIndicator(
        progress = { animated },
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp),
        color = Color(0xFF00E5FF),
        trackColor = Color.Transparent
    )
}