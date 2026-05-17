package com.kingfisher.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MenuSheet(
    onClose: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🌑 Background dim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { onClose() }
        )

        // 📱 Sheet
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // handle bar
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant,
                            RoundedCornerShape(50)
                        )
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "About Me",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "👤 Name: Md. Nazmul Hossain\n📱 Browser: Kingfisher Browser\n🚀 Version: 3.0.0 (Beta)\n💡 Built with Jetpack Compose + GeckoView",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(20.dp))

                HorizontalDivider()

                Spacer(Modifier.height(12.dp))

                MenuItem("Settings")
                MenuItem("Downloads")
                MenuItem("History")
                MenuItem("Privacy")
            }
        }
    }
}