package com.kingfisher.browser.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit
) {

    if (!show) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF141A2E)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text("AI Assistant", color = Color.White)

            Spacer(Modifier.height(16.dp))

            Text("Ask anything...", color = Color.Gray)

        }
    }
}