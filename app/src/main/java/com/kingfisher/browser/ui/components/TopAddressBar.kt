package com.kingfisher.browser.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
    onReload: () -> Unit,
    onFocusChange: (Boolean) -> Unit
) {
    var input by rememberSaveable { mutableStateOf(url) }
    var isFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(url) {
        input = url
    }

    // ✨ Chrome-like animations
    val shape by animateDpAsState(
        targetValue = if (isFocused) 28.dp else 18.dp,
        label = ""
    )

    val elevation by animateDpAsState(
        targetValue = if (isFocused) 6.dp else 0.dp,
        label = ""
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.02f else 1f,
        label = ""
    )

    Surface(
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        shape = RoundedCornerShape(shape),
        tonalElevation = elevation,
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

                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        isFocused = it.isFocused
                        onFocusChange(it.isFocused) // ✅ ADD THIS HERE
                    },

                placeholder = {
                    Text(
                        "Search or address",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },

                singleLine = true,

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    cursorColor = AccentCyan,

                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
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