package com.kingfisher.browser.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.kingfisher.browser.browser.engine.GeckoEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

class BrowserViewModel(
    val engine: GeckoEngine
) : ViewModel() {

    private var session: GeckoSession? = null

    private val _urlBar = MutableStateFlow("")
    val urlBar: StateFlow<String> = _urlBar

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private val _pageTitle = MutableStateFlow("")
    val pageTitle: StateFlow<String> = _pageTitle

    init {
        // 📊 Progress updates
        engine.onProgressChanged = { value ->
            _progress.value = value / 100f
        }

        // 🌐 URL updates
        engine.onUrlChanged = { url ->
            _urlBar.value = url
        }

        // 📄 Title updates
        engine.onTitleChanged = { title ->
            _pageTitle.value = title
        }
    }

    fun onUrlChange(text: String) {
        _urlBar.value = text
    }

    fun onGo() {
        val input = _urlBar.value.trim()
        if (input.isEmpty()) return

        val url = if (input.startsWith("http://") || input.startsWith("https://")) {
            input
        } else {
            "https://$input"
        }

        engine.loadUrl(url)
    }

    fun goBack() = engine.goBack()

    fun goForward() = engine.goForward()

    fun reload() = engine.reload()

    fun canGoBack(): Boolean = engine.canGoBack()
}