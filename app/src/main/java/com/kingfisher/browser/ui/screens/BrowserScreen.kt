package com.kingfisher.browser.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kingfisher.browser.ui.components.*
import com.kingfisher.browser.ui.viewmodels.BrowserViewModel

@Composable
fun BrowserScreen(
    viewModel: BrowserViewModel
) {
    val context = LocalContext.current

    var showAi by remember { mutableStateOf(false) }

    val progress by viewModel.progress.collectAsState()

    // ----------------------------
    // BACK BUTTON (SAFE)
    // ----------------------------
    BackHandler {
        if (viewModel.canGoBack()) {
            viewModel.goBack()
        } else {
            (context as Activity).finish()
        }
    }

    // ----------------------------
    // MAIN UI
    // ----------------------------
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F1A))
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔝 ADDRESS BAR
            TopAddressBar(viewModel)

            // 📊 LOADING PROGRESS
            LoadingProgressBar(progress = progress)

            // 🌐 WEB VIEW
            GeckoViewContainer(
                engine = viewModel.engine,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            // 🔽 BOTTOM BAR
            BottomBar(viewModel)
        }

        // 🤖 AI ORB (BOTTOM CENTER)
        AiOrbButton(
            onClick = { showAi = true },
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 24.dp)
        )

        // 🧠 AI BOTTOM SHEET
        AiBottomSheet(
            show = showAi,
            onDismiss = { showAi = false }
        )
    }
}