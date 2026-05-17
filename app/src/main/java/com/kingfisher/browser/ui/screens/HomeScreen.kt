package com.kingfisher.browser.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kingfisher.browser.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kingfisher.browser.domain.model.ShortcutSite
import com.kingfisher.browser.ui.components.ShortcutItem
import com.kingfisher.browser.ui.theme.AccentCyan

@Composable
fun HomeScreen(
    onSearch: (String) -> Unit,
    onShortcutClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }

    val customShortcuts = remember {
        listOf(
            ShortcutSite(name = "GitHub", url = "https://github.com/nazmul-1117"),
            ShortcutSite(name = "Nazmul-1117", url = "https://nazmul-1117.github.io/"),
            ShortcutSite(name = "GUB", url = "https://www.green.edu.bd/"),
            ShortcutSite(name = "YouTube", url = "https://youtube.com")
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 1. BACKGROUND IMAGE LAYER
        Image(
            painter = painterResource(id = R.drawable.home_bg), // Replace with your image asset
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. DIMMING OVERLAY (Ensures text/search bar remain visible)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // 3. CONTENT LAYER
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Kingfisher",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Light,
                    letterSpacing = 1.5.sp,
                    color = AccentCyan // Keep your brand accent pop color
                )
            )
            Text(
                text = "Fast. Private. Yours.",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 4.dp, bottom = 40.dp)
            )

            // Glassmorphic Search Bar
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.15f), // True glassmorphism uses transparent white layers
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.width(8.dp))
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search or enter address", color = Color.White.copy(0.6f)) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = AccentCyan
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            if (query.isNotBlank()) {
                                onSearch(query)
                                focusManager.clearFocus()
                            }
                        })
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // CUSTOM SHORTCUTS GRID
            // Using a simple Chunked Row layout inside our scrollable Column
            customShortcuts.chunked(4).forEach { rowSites ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowSites.forEach { site ->
                        ShortcutItem(
                            site = site,
                            onClick = { onShortcutClick(site.url) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill remaining space if the last row is incomplete
                    if (rowSites.size < 4) {
                        Spacer(modifier = Modifier.weight((4 - rowSites.size).toFloat()))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}