package com.kingfisher.browser.browser.engine

import android.content.Context
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
//import GeckoSession.NavigationDelegate.AllowOrDeny

data class EngineState(
    val currentUrl: String? = null,
    val title: String? = null,
    val progress: Int = 0,
    val isLoading: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false
)

class GeckoEngine(private val context: Context) {

    private val _state = MutableStateFlow(EngineState())
    val state: StateFlow<EngineState> = _state.asStateFlow()

    lateinit var runtime: GeckoRuntime
        private set

    private var session: GeckoSession? = null

    init {

        val runtimeSettings = GeckoRuntimeSettings.Builder()
            .javaScriptEnabled(true)
            .remoteDebuggingEnabled(false)
            .build()

        // Tracking Protection is NOT set on runtime in most GeckoView versions
        // It is session-level or controlled via ContentBlockingController

        val sessionSettings = GeckoSessionSettings.Builder()
            .useTrackingProtection(true)
            .build()

        runtime = GeckoRuntime.create(context, runtimeSettings)

        createSession(sessionSettings)
    }

    private fun createSession(settings: GeckoSessionSettings) {
        session?.let {
            it.close()
        }

        val newSession = GeckoSession(settings).apply {
            open(runtime)
        }

        attachDelegates(newSession)

        session = newSession
        _state.value = EngineState()
    }

    private fun attachDelegates(session: GeckoSession) {

        session.navigationDelegate = object : GeckoSession.NavigationDelegate {

            override fun onLocationChange(
                session: GeckoSession,
                url: String?,
                perms: MutableList<GeckoSession.PermissionDelegate.ContentPermission>,
                hasUserGesture: Boolean
            ) {
                _state.value = _state.value.copy(
                    currentUrl = url
                )
            }

            override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
                _state.value = _state.value.copy(
                    canGoBack = canGoBack
                )
            }

            override fun onCanGoForward(session: GeckoSession, canGoForward: Boolean) {
                _state.value = _state.value.copy(
                    canGoForward = canGoForward
                )
            }

            override fun onLoadRequest(
                session: GeckoSession,
                request: GeckoSession.NavigationDelegate.LoadRequest
            ): GeckoResult<AllowOrDeny>? {

                // 1. Update your UI state safely to indicate loading has begun
                _state.value = _state.value.copy(
                    isLoading = true,
                    progress = 0
                )

                // 2. Return null to signal to GeckoView to process the URL navigation normally
                return null
            }
        }

        session.progressDelegate = object : GeckoSession.ProgressDelegate {

            override fun onPageStart(session: GeckoSession, url: String) {
                _state.value = _state.value.copy(
                    isLoading = true,
                    progress = 0
                )
            }

            override fun onPageStop(session: GeckoSession, success: Boolean) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    progress = 100
                )
            }

            override fun onProgressChange(session: GeckoSession, progress: Int) {
                val current = _state.value
                if (current.progress != progress) {
                    _state.value = current.copy(progress = progress)
                }
            }
        }
    }

    fun loadUrl(input: String) {
        val sanitized = sanitizeUrl(input)
        session?.loadUri(sanitized)
    }

    fun reload() = session?.reload()
    fun goBack() = session?.goBack()
    fun goForward() = session?.goForward()
    fun stop() = session?.stop()

    fun getSession(): GeckoSession =
        session ?: error("Session not initialized")

    private fun sanitizeUrl(input: String): String {
        val trimmed = input.trim()

        val hasScheme = trimmed.startsWith("http://", true) ||
                trimmed.startsWith("https://", true)

        val looksLikeUrl =
            trimmed.contains(".") &&
                    !trimmed.contains(" ")

        return when {
            hasScheme -> trimmed
            looksLikeUrl -> "https://$trimmed"
            else -> "https://duckduckgo.com/?q=${trimmed.replace(" ", "+")}"
        }
    }

    fun onPause() {
        session?.setActive(false)
    }

    fun onResume() {
        session?.setActive(true)
    }

    fun onDestroy() {
        session?.let {
            it.stop()
            it.close()
        }
        session = null
    }
}
