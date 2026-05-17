package com.kingfisher.browser.browser.engine

data class EngineState(
    val currentUrl: String? = null,
    val title: String? = null,
    val isLoading: Boolean = false,
    val progress: Int = 0,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false
)