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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.kingfisher.browser.ui.components.MenuSheet

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
    var isMenuOpen by remember { mutableStateOf(false) }

    val bottomBarAlpha by animateFloatAsState(
        targetValue = if (isSearchMode) 0f else 1f,
        label = ""
    )

    val webScale by animateFloatAsState(
        targetValue = if (isSearchMode) 0.98f else 1f,
        label = ""
    )

    val webAlpha by animateFloatAsState(
        targetValue = if (isSearchMode) 0.2f else 1f,
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {

        // 🌐 WEB CONTENT (BOTTOM LAYER)
        AndroidView(
            factory = { ctx ->
                org.mozilla.geckoview.GeckoView(ctx).apply {
                    setSession(engine.getSession())
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = webAlpha
                    scaleX = webScale
                    scaleY = webScale
                }
        )

        // 🌑 DIM OVERLAY (SEARCH OR MENU)
        if (isSearchMode || isMenuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
        }

        // 🔝 TOP ADDRESS BAR (FLOATING)
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

        // ⬇️ BOTTOM BAR (FLOATING, NO LAYOUT SHIFT)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    alpha = bottomBarAlpha
                    translationY = if (isSearchMode) 100f else 0f
                }
        ) {
            BottomNavigationBar(
                canGoBack = state.canGoBack,
                canGoForward = state.canGoForward,
                onBack = onBack,
                onForward = onForward,
                onMenuClick = { isMenuOpen = true }
            )
        }

        // 🍔 MENU SHEET (TOP LAYER)
        if (isMenuOpen) {
            MenuSheet(
                onClose = { isMenuOpen = false }
            )
        }
    }

    // 🔙 SYSTEM BACK HANDLER (CHROME-LIKE BEHAVIOR)
    BackHandler(enabled = true) {
        when {
            isMenuOpen -> isMenuOpen = false
            isSearchMode -> isSearchMode = false
            state.canGoBack -> onBack()
            else -> {
                // allow system exit
            }
        }
    }
}
