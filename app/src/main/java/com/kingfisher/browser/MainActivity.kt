package com.kingfisher.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge  // 🟢 Make sure to import this!
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.*
import com.kingfisher.browser.ui.screens.BrowserScreen
import com.kingfisher.browser.ui.screens.HomeScreen
import com.kingfisher.browser.ui.theme.KingfisherTheme
import com.kingfisher.browser.ui.viewmodels.BrowserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: BrowserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🟢 This replaces the deprecated code from your theme file perfectly!
        // It makes status/navigation bars transparent and sets the system icons
        // to a clean light color suitable for a deep dark theme automatically.
        enableEdgeToEdge()

        setContent {
            KingfisherTheme {
                val uiState by viewModel.uiState.collectAsState()

                AnimatedContent(targetState = uiState.screenMode, label = "ScreenTransition") { mode ->
                    when (mode) {
                        BrowserViewModel.ScreenMode.HOME -> HomeScreen(onSearch = viewModel::onUrlSubmit)
                        BrowserViewModel.ScreenMode.BROWSER -> BrowserScreen(
                            state = uiState.engineState,
                            onUrlSubmit = viewModel::onUrlSubmit,
                            onBack = viewModel::onBack,
                            onForward = viewModel::onForward,
                            onReload = viewModel::onReload,
                            onHome = viewModel::onNavigateHome,
                            onInputChange = viewModel::onInputChanged,
                            engine = viewModel.engine
                        )
                    }
                }
            }
        }
    }
}