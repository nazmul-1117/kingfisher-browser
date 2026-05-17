package com.kingfisher.browser.browser.engine

interface BrowserEngine {

    fun loadUrl(input: String)

    fun goBack()

    fun goForward()

    fun reload()

    fun destroy()
}