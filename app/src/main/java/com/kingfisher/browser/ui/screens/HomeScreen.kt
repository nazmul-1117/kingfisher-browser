package com.kingfisher.browser.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kingfisher.browser.ui.theme.AccentCyan
import com.kingfisher.browser.ui.theme.AccentPurple
import com.kingfisher.browser.ui.theme.BackgroundDark
import com.kingfisher.browser.ui.theme.SurfaceDark

@Composable
fun HomeScreen(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
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
                color = AccentCyan
            )
        )
        Text(
            text = "Fast. Private. Yours.",
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp, bottom = 40.dp)
        )

        // Glassmorphic Search Bar
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceDark.copy(alpha = 0.4f),
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search, // Fixed here
                    contentDescription = null,
                    tint = AccentCyan.copy(alpha = 0.7f)
                )
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search or enter address", color = Color.White.copy(0.4f)) },
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

        // Quick Actions (Prepared for AI/Features)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            QuickChip("AI Summarize", accent = AccentPurple)
            QuickChip("Privacy", accent = AccentCyan)
        }
    }
}

@Composable
private fun QuickChip(label: String, accent: Color) {
    Surface(
        modifier = Modifier
            .width(130.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { },
        color = SurfaceDark.copy(0.5f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, color = Color.White, style = MaterialTheme.typography.labelLarge)
        }
    }
}