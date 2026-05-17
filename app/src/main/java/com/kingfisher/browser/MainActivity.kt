package com.kingfisher.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kingfisher.browser.browser.engine.GeckoEngine
import com.kingfisher.browser.ui.screens.BrowserScreen
import com.kingfisher.browser.ui.theme.KingfisherBrowserTheme
import com.kingfisher.browser.ui.viewmodels.BrowserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // 1. Create engine FIRST
        val engine = GeckoEngine(applicationContext)

        // 2. IMPORTANT: initialize Gecko BEFORE UI uses it
        engine.init()

        setContent {

            KingfisherBrowserTheme {

                // 3. ViewModel
                val viewModel: BrowserViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return BrowserViewModel(engine) as T
                        }
                    }
                )

                // 4. UI
                BrowserScreen(viewModel = viewModel)
            }
        }
    }
}