package com.kingfisher.browser.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.kingfisher.browser.ui.theme.AccentCyan

@Composable
fun TopAddressBar(
    modifier: Modifier = Modifier,
    url: String,
    isLoading: Boolean,
    onUrlSubmit: (String) -> Unit,
    onInputChange: (String) -> Unit,
    onHomeClick: () -> Unit,
    onReload: () -> Unit
) {
    var input by rememberSaveable { mutableStateOf(url) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(url) {
        input = url
    }

    Surface(
        modifier = modifier,
        tonalElevation = 0.dp, // ✅ IMPORTANT FIX (removes cyan tint)
        color = MaterialTheme.colorScheme.surface
    ) {

        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                onHomeClick()
                focusManager.clearFocus()
            }) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            TextField(
                value = input,
                onValueChange = {
                    input = it
                    onInputChange(it)
                },
                modifier = Modifier.weight(1f),

                placeholder = {
                    Text(
                        "Search or address",
                        color = MaterialTheme.colorScheme.onSurfaceVariant // ✅ FIX
                    )
                },

                singleLine = true,

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    cursorColor = AccentCyan,

                    focusedTextColor = MaterialTheme.colorScheme.onSurface,   // ❌ FIXED
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface  // ❌ FIXED
                ),

                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),

                keyboardActions = KeyboardActions(
                    onSearch = {
                        onUrlSubmit(input)
                        focusManager.clearFocus()
                    }
                )
            )

            IconButton(onClick = onReload) {
                Icon(
                    imageVector =
                        if (isLoading) Icons.Default.Close
                        else Icons.Default.Refresh,

                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}