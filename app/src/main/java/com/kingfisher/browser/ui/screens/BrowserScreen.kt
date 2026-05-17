package com.kingfisher.browser.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.graphics.graphicsLayer

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

    var isSearchMode by remember { mutableStateOf(false) }

    val bottomBarAlpha by animateFloatAsState(
        targetValue = if (isSearchMode) 0f else 1f,
        label = ""
    )

    val webContentAlpha by animateFloatAsState(
        targetValue = if (isSearchMode) 0.2f else 1f,
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        // 🔥 TOP ADDRESS BAR (EXPANDS INTO SEARCH MODE)
        TopAddressBar(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            url = state.currentUrl ?: "",
            isLoading = state.isLoading,
            onUrlSubmit = onUrlSubmit,
            onInputChange = onInputChange,
            onHomeClick = onHome,
            onReload = onReload,
            onFocusChange = { focused ->
                isSearchMode = focused
            }
        )

        // 🔥 PROGRESS BAR
        BrowserProgressBar(
            isLoading = state.isLoading,
            progress = state.progress
        )

        // 🔥 WEB CONTENT (DIM WHEN SEARCH MODE)
        Box(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    alpha = webContentAlpha
                    scaleX = if (isSearchMode) 0.98f else 1f
                    scaleY = if (isSearchMode) 0.98f else 1f
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

        // 🔥 BOTTOM BAR (HIDES IN SEARCH MODE)
        Box(
            modifier = Modifier
                .graphicsLayer {
                    alpha = bottomBarAlpha
                    translationY = if (isSearchMode) 100f else 0f
                }
        ) {
            BottomNavigationBar(
                canGoBack = state.canGoBack,
                canGoForward = state.canGoForward,
                onBack = onBack,
                onForward = onForward
            )
        }
    }
    BackHandler(enabled = true) {
        when {
            state.canGoBack -> onBack()
            isSearchMode -> isSearchMode = false // close search first
            else -> {
                // allow system to exit app
            }
        }
    }
}
