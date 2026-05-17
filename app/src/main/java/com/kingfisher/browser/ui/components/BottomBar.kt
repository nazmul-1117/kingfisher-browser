package com.kingfisher.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kingfisher.browser.ui.viewmodels.BrowserViewModel

@Composable
fun BottomBar(viewModel: BrowserViewModel) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFF141A2E)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BottomIcon(Icons.Default.Add, "New Tab") { }
        BottomIcon(Icons.Default.Bookmark, "Bookmark") { }
        BottomIcon(Icons.Default.SmartToy, "AI Chat") { }
        BottomIcon(Icons.Default.Tab, "Tabs") { }
        BottomIcon(Icons.Default.Menu, "Menu") { }
    }
}

@Composable
fun BottomIcon(
    icon: ImageVector,
    desc: String,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = desc,
            tint = Color(0xFF8E24AA)
        )
    }
}