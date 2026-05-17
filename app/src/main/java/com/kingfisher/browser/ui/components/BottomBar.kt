package com.kingfisher.browser.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    canGoBack: Boolean,
    canGoForward: Boolean,
    onBack: () -> Unit,
    onForward: () -> Unit
) {
    Surface(
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surface
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔙 NAVIGATION GROUP (LEFT)
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                IconButton(
                    onClick = onBack,
                    enabled = canGoBack,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (canGoBack)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onForward,
                    enabled = canGoForward,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Forward",
                        tint = if (canGoForward)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ⭐ CENTER ACTIONS (MAIN FEATURES)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                IconButton(
                    onClick = { /* new tab */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "New Tab",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = { /* bookmarks */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = { /* AI chat */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.SmartToy, // AI icon
                        contentDescription = "AI",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // ⚙️ RIGHT SIDE (TAB + MENU)
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                IconButton(
                    onClick = { /* tab switcher */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text("1") // later bind tab count
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Tab,
                            contentDescription = "Tabs",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                IconButton(
                    onClick = { /* menu */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}