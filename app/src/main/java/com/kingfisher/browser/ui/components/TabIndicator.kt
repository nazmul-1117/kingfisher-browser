package com.kingfisher.browser.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TabIndicator(tabCount: Int) {
    Box {
        Icon(
            imageVector = Icons.Default.Tab,
            contentDescription = "Tabs"
        )

        if (tabCount > 0) {
            Badge {
                Text(tabCount.toString())
            }
        }
    }
}