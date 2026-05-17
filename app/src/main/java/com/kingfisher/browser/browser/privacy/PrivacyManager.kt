package com.kingfisher.browser.browser.privacy

import org.mozilla.geckoview.GeckoSession
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrivacyManager @Inject constructor(
    private val settings: PrivacySettingsManager,
    private val trackerBlocker: TrackerBlocker,
    private val httpsEnforcer: HttpsEnforcer,
    private val cookieController: CookieController
) {

    fun applyToSession(session: GeckoSession) {

        val current = settings.settings.value

        // -------------------------
        // COOKIE CONTROL
        // -------------------------
        cookieController.setAcceptCookies(!current.blockCookies)
        cookieController.setThirdPartyCookies(!current.blockCookies)

        if (current.incognitoMode) {
            cookieController.enableIncognitoMode()
        }

        // -------------------------
        // TRACKING PROTECTION (REAL APPLICATION)
        // -------------------------
        session.settings.apply {
            useTrackingProtection = true
        }
    }

    fun shouldBlockUrl(url: String): Boolean {
        return trackerBlocker.isBlocked(url)
    }

    fun enforceHttps(url: String): String {
        return httpsEnforcer.enforce(url, enabled = true)
    }
}