package com.kingfisher.browser.browser.privacy

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpsEnforcer @Inject constructor() {

    fun enforce(url: String, enabled: Boolean): String {
        if (!enabled) return url

        return if (url.startsWith("http://")) {
            url.replace("http://", "https://")
        } else {
            url
        }
    }

    fun isSecure(url: String): Boolean {
        return url.startsWith("https://")
    }
}