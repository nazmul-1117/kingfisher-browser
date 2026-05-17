package com.kingfisher.browser.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.kingfisher.browser.browser.engine.EngineState
import com.kingfisher.browser.browser.engine.GeckoEngine
import com.kingfisher.browser.ui.theme.AccentCyan
import com.kingfisher.browser.ui.theme.SurfaceDark

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
            .background(SurfaceDark)
    ) {

        AddressBar(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            url = state.currentUrl ?: "",
            progress = state.progress,
            isLoading = state.isLoading,
            onUrlSubmit = onUrlSubmit,
            onInputChange = onInputChange,
            onHomeClick = onHome,
            onReload = onReload
        )

        // Progress Indicator
        // Fixed: Compose expects a lambda returning float dynamically or standard Float state
        LinearProgressIndicator(
            progress = { if (state.isLoading) state.progress / 100f else 0f },
            modifier = Modifier.height(3.dp),
            color = AccentCyan,
            trackColor = Color.Transparent
        )

        // GeckoView Viewport Container
        Box(Modifier.weight(1f)) {
            AndroidView(
                factory = { ctx ->
                    org.mozilla.geckoview.GeckoView(ctx).apply {
                        setBackgroundColor(android.graphics.Color.TRANSPARENT)
                        setSession(engine.getSession())
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            // Empty state loading placeholder
            if (state.currentUrl == null) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = AccentCyan.copy(0.5f))
                }
            }
        }

        // Bottom Navigation Bar
        BottomNavigationBar(
            canGoBack = state.canGoBack,
            canGoForward = state.canGoForward,
            onBack = onBack,
            onForward = onForward
        )
    }
}

@Composable
private fun AddressBar(
    modifier: Modifier = Modifier,
    url: String,
    progress: Int,
    isLoading: Boolean,
    onUrlSubmit: (String) -> Unit,
    onInputChange: (String) -> Unit,
    onHomeClick: () -> Unit,
    onReload: () -> Unit
) {
    var input by rememberSaveable { mutableStateOf(url) }
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    LaunchedEffect(url) {
        input = url
    }

    Surface(
        modifier = modifier,
        tonalElevation = 8.dp,
        color = SurfaceDark.copy(0.8f)
    ) {
        Row(
            Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                onHomeClick()
                focusManager.clearFocus()
            }) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
            }

            TextField(
                value = input,
                onValueChange = {
                    input = it
                    onInputChange(it)
                },
                placeholder = {
                    Text("Search or address", color = Color.Gray)
                },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = AccentCyan,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onUrlSubmit(input)
                        focusManager.clearFocus()
                    }
                )
            )

            IconButton(onClick = { onReload() }) {
                Icon(
                    imageVector = if (isLoading) Icons.Default.Close else Icons.Default.Refresh,
                    contentDescription = if (isLoading) "Stop" else "Reload",
                    tint = AccentCyan
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    canGoBack: Boolean,
    canGoForward: Boolean,
    onBack: () -> Unit,
    onForward: () -> Unit
) {
    Surface(tonalElevation = 4.dp, color = SurfaceDark.copy(0.9f)) {
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = onBack, enabled = canGoBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = if (canGoBack) Color.White else Color.Gray)
            }
            IconButton(onClick = onForward, enabled = canGoForward) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "Forward", tint = if (canGoForward) Color.White else Color.Gray)
            }
        }
    }
}