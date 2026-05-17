package com.kingfisher.browser.browser.engine

import android.content.Context
import android.util.Patterns
import com.kingfisher.browser.browser.privacy.PrivacyManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mozilla.geckoview.*

class GeckoEngine(
    private val context: Context,
    private val privacyManager: PrivacyManager
) {

    private val _state = MutableStateFlow(EngineState())
    val state: StateFlow<EngineState> = _state.asStateFlow()

    val runtime: GeckoRuntime

    private var session: GeckoSession? = null

    init {

        val contentBlockingSettings = ContentBlocking.Settings.Builder()
            .cookieBehavior(ContentBlocking.CookieBehavior.ACCEPT_NON_TRACKERS)
            .build()

        val runtimeSettings = GeckoRuntimeSettings.Builder()
            .javaScriptEnabled(true)
            .remoteDebuggingEnabled(false)
            .contentBlocking(contentBlockingSettings)
            .build()

        runtime = GeckoRuntime.create(context, runtimeSettings)

        createSession()
    }

    // ---------------------------
    // SESSION
    // ---------------------------
    private fun createSession() {
        session?.close()
        session = null

        val newSession = GeckoSession().apply {
            open(runtime)
        }

        attachDelegates(newSession)
        session = newSession

        _state.value = EngineState()
    }

    // ---------------------------
    // DELEGATES
    // ---------------------------
    private fun attachDelegates(session: GeckoSession) {

        privacyManager.applyToSession(session)

        session.navigationDelegate = object : GeckoSession.NavigationDelegate {

            override fun onLoadRequest(
                session: GeckoSession,
                request: GeckoSession.NavigationDelegate.LoadRequest
            ): GeckoResult<AllowOrDeny>? {

                val originalUrl = request.uri
                    ?: return GeckoResult.fromValue(AllowOrDeny.ALLOW)

                val secureUrl = privacyManager.enforceHttps(originalUrl)

                if (privacyManager.shouldBlockUrl(secureUrl)) {
                    return GeckoResult.fromValue(AllowOrDeny.DENY)
                }

                // HTTPS upgrade redirect
                if (secureUrl != originalUrl) {
                    session.loadUri(secureUrl)
                    return GeckoResult.fromValue(AllowOrDeny.DENY)
                }

                return GeckoResult.fromValue(AllowOrDeny.ALLOW)
            }

            override fun onLocationChange(
                session: GeckoSession,
                url: String?,
                perms: MutableList<GeckoSession.PermissionDelegate.ContentPermission>,
                hasUserGesture: Boolean
            ) {
                _state.value = _state.value.copy(currentUrl = url)
            }
        }

        session.contentDelegate = object : GeckoSession.ContentDelegate {

            override fun onTitleChange(session: GeckoSession, title: String?) {
                _state.value = _state.value.copy(title = title)
            }
        }

        session.progressDelegate = object : GeckoSession.ProgressDelegate {

            override fun onProgressChange(
                session: GeckoSession,
                progress: Int
            ) {
                _state.value = _state.value.copy(
                    isLoading = progress in 1..99
                )
            }

            override fun onPageStop(
                session: GeckoSession,
                success: Boolean
            ) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    // ---------------------------
    // PUBLIC API
    // ---------------------------
    fun loadUrl(input: String) {
        val url = sanitizeUrl(input)
        session?.loadUri(url)
    }

    fun reload() = session?.reload()

    fun goBack() = session?.goBack()

    fun goForward() = session?.goForward()

    fun stop() = session?.stop()

    fun getSession(): GeckoSession =
        session ?: error("GeckoSession not initialized")

    fun getSessionOrNull(): GeckoSession? = session

    fun updatePrivacy() {
        session?.let { privacyManager.applyToSession(it) }
    }

    // ---------------------------
    // URL HANDLING
    // ---------------------------
    private fun sanitizeUrl(input: String): String {
        val trimmed = input.trim()

        return when {
            trimmed.startsWith("http://", true) ||
                    trimmed.startsWith("https://", true) -> {
                privacyManager.enforceHttps(trimmed)
            }

            Patterns.WEB_URL.matcher(trimmed).matches() -> {
                "https://$trimmed"
            }

            else -> {
                "https://duckduckgo.com/?q=${
                    java.net.URLEncoder.encode(trimmed, "UTF-8")
                }"
            }
        }
    }

    // ---------------------------
    // LIFECYCLE
    // ---------------------------
    fun onPause() {
        // no-op in most GeckoView versions
    }

    fun onResume() {
        // no-op in most GeckoView versions
    }

    fun onDestroy() {
        session?.close()
        session = null
    }
}