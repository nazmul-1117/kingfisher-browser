package com.kingfisher.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
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

        enableEdgeToEdge()

        setContent {

            val darkTheme = isSystemInDarkTheme()

            // ✅ Correct place for system UI control
            val window = window

            DisposableEffect(darkTheme) {
                val controller = WindowInsetsControllerCompat(window, window.decorView)

                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme

                WindowCompat.setDecorFitsSystemWindows(window, false)

                onDispose { }
            }

            KingfisherTheme {

                val uiState by viewModel.uiState.collectAsState()

                AnimatedContent(
                    targetState = uiState.screenMode,
                    label = "ScreenTransition"
                ) { mode ->

                    when (mode) {

                        BrowserViewModel.ScreenMode.HOME ->
                            HomeScreen(
                                onSearch = viewModel::onUrlSubmit
                            )

                        BrowserViewModel.ScreenMode.BROWSER ->
                            BrowserScreen(
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