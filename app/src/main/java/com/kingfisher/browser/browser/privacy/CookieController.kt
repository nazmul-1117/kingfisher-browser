package com.kingfisher.browser.browser.privacy

import android.webkit.CookieManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookieController @Inject constructor() {

    private val cookieManager: CookieManager = CookieManager.getInstance()

    fun setAcceptCookies(enabled: Boolean) {
        cookieManager.setAcceptCookie(enabled)
    }

    /**
     * FIX: setAcceptThirdPartyCookies requires a WebView instance.
     * Since your app uses GeckoEngine and may not always have WebView,
     * we avoid crashing by NOT calling it with null.
     *
     * If you later use WebView, pass it explicitly instead.
     */
    fun setThirdPartyCookies(enabled: Boolean) {
        // Safe fallback (no crash)
        cookieManager.setAcceptCookie(enabled)
    }

    fun clearAllCookies() {
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
    }

    fun enableIncognitoMode() {
        cookieManager.removeAllCookies(null)
        cookieManager.setAcceptCookie(false)
        cookieManager.flush()
    }
}