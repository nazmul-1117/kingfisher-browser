package com.kingfisher.browser.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.kingfisher.browser.browser.engine.EngineState
import com.kingfisher.browser.browser.engine.GeckoEngine
import com.kingfisher.browser.ui.components.BottomNavigationBar
import com.kingfisher.browser.ui.components.BrowserProgressBar
import com.kingfisher.browser.ui.components.TopAddressBar
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs
import androidx.activity.compose.BackHandler

@Composable
fun BrowserScreen(
    state: EngineState,
    onUrlSubmit: (String) -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onReload: () -> Unit,
    onHome: () -> Unit,
    onInputChange: (String) -> Unit,
    engine: GeckoEngine
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        TopAddressBar(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            url = state.currentUrl ?: "",
            isLoading = state.isLoading,
            onUrlSubmit = onUrlSubmit,
            onInputChange = onInputChange,
            onHomeClick = onHome,
            onReload = onReload
        )

        BrowserProgressBar(
            isLoading = state.isLoading,
            progress = state.progress
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .pointerInput(Unit) {

                    var totalDragX = 0f

                    detectHorizontalDragGestures(
                        onDragEnd = {
                            val threshold = 150f

                            when {
                                totalDragX > threshold -> {
                                    onBack()
                                }
                                totalDragX < -threshold -> {
                                    onForward()
                                }
                            }

                            totalDragX = 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            totalDragX += dragAmount
                        }
                    )
                }
        ) {
            AndroidView(
                factory = { ctx ->
                    org.mozilla.geckoview.GeckoView(ctx).apply {
                        setSession(engine.getSession())
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        BottomNavigationBar(
            canGoBack = state.canGoBack,
            canGoForward = state.canGoForward,
            onBack = onBack,
            onForward = onForward
        )
    }
    BackHandler(enabled = true) {
        when {
            state.canGoBack -> onBack()
            else -> {
                // optional: show "exit app" or do nothing
                // system will exit naturally
            }
        }
    }
}