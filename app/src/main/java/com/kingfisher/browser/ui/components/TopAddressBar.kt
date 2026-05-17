package com.kingfisher.browser.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.kingfisher.browser.ui.viewmodels.BrowserViewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun TopAddressBar(
    viewModel: BrowserViewModel
) {

    val urlState by viewModel.urlBar.collectAsState()
    val titleState by viewModel.pageTitle.collectAsState()

    // 🌐 SMART DISPLAY (FIXED)
    val displayText = urlState.ifBlank { titleState }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(26.dp),
        color = Color(0xFF141A2E),
        tonalElevation = 6.dp
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {

            // 🔍 ICON
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF00E5FF),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 🌐 OMNIBOX
            TextField(
                value = urlState,
                onValueChange = {
                    viewModel.onUrlChange(it)
                },
                placeholder = {
                    Text(
                        text = displayText.ifBlank { "Search or enter URL" },
                        color = Color.Gray
                    )
                },
                modifier = Modifier.weight(1f),
                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go
                ),

                keyboardActions = KeyboardActions(
                    onGo = {
                        viewModel.onGo()
                    }
                ),

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF00E5FF),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // ❌ CLEAR
            if (urlState.isNotEmpty()) {
                IconButton(
                    onClick = {
                        viewModel.onUrlChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}