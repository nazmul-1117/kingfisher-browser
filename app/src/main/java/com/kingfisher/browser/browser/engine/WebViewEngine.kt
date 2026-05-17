package com.kingfisher.browser.browser.engine

import android.webkit.WebView
import com.kingfisher.browser.util.UrlUtils

class WebViewEngine(
    private val webView: WebView
) : BrowserEngine {

    override fun loadUrl(input: String) {
        webView.loadUrl(input)
    }

    override fun goBack() {
        webView.goBack()
    }

    override fun goForward() {
        webView.goForward()
    }

    override fun reload() {
        webView.reload()
    }

    override fun destroy() {
        webView.destroy()
    }
}