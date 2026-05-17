package com.kingfisher.browser.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.kingfisher.browser.browser.engine.EngineState
import com.kingfisher.browser.browser.engine.GeckoEngine
import com.kingfisher.browser.ui.components.BottomNavigationBar
import com.kingfisher.browser.ui.components.TopAddressBar
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        // 📦 MAIN LAYOUT (FIXED INSETS HERE)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()        // ✅ FIX: prevents overlap with clock/wifi
                .navigationBarsPadding()    // ✅ FIX: prevents bottom system overlap
        ) {

            // 🔝 TOP ADDRESS BAR (SAFE NOW)
            TopAddressBar(
                modifier = Modifier.fillMaxWidth(),
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

            // 🌐 WEB CONTENT (TAKES REMAINING SPACE)
            AndroidView(
                factory = { ctx ->
                    org.mozilla.geckoview.GeckoView(ctx).apply {
                        setSession(engine.getSession())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        // 🌑 OVERLAY (SEARCH / MENU DIM)
        if (isSearchMode || isMenuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
        }

        // ⬇️ BOTTOM NAV BAR
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigationBar(
                canGoBack = state.canGoBack,
                canGoForward = state.canGoForward,
                onBack = onBack,
                onForward = onForward,
                onMenuClick = { isMenuOpen = true }
            )
        }

        // 🍔 MENU SHEET
        if (isMenuOpen) {
            MenuSheet(
                onClose = { isMenuOpen = false }
            )
        }
    }

    // 🔙 BACK HANDLER
    BackHandler(enabled = true) {
        when {
            isMenuOpen -> isMenuOpen = false
            isSearchMode -> isSearchMode = false
            state.canGoBack -> onBack()
        }
    }
}