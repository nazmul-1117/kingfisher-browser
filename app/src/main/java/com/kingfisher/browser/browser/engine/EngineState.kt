package com.kingfisher.browser.browser.engine

data class EngineState(
    val currentUrl: String? = null,
    val title: String? = null,
    val isLoading: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false
)