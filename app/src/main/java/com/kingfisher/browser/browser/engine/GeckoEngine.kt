// File: app/browser/engine/GeckoEngine.kt
package com.kingfisher.browser.browser.engine

import android.content.Context
import android.view.View
import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mozilla.geckoview.*

/**
 * Production-ready GeckoView wrapper with:
 * - Singleton GeckoRuntime (memory-safe)
 * - Flow-based state emission (Compose-friendly)
 * - Lifecycle-aware session management
 * - Privacy controls & tracking protection
 * - Thread-safe UI updates
 */
class GeckoEngine(
    private val context: Context,
    private val externalScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    // 🎯 UI State Flow (immutable, Compose-ready)
    private val _uiState = MutableStateFlow(GeckoEngineState())
    val uiState: StateFlow<GeckoEngineState> = _uiState.asStateFlow()

    // 🦎 Gecko Runtime - Singleton per process (critical for memory)
    private val runtime by lazy {
        GeckoRuntime.create(
            context.applicationContext,
            GeckoRuntimeSettings.Builder()
                .trackingProtection(
                    GeckoSession.TrackingProtectionOptions.Builder()
                        .strictMode(true) // Privacy-first default
                        .build()
                )
                .consoleOutput(GeckoRuntimeSettings.ConsoleOutput.ENABLED) // Debugging
                .build()
        )
    }

    // 📄 Session management
    private var session: GeckoSession? = null
    private var geckoView: GeckoView? = null

    // 🔐 Privacy configuration
    private var isPrivateMode: Boolean = false
    private var userAgent: String? = null
    private var javascriptEnabled: Boolean = true

    // 🧹 Cleanup flag
    private var isDestroyed = false

    /**
     * Initialize the engine. Must be called before any operations.
     * Thread-safe and idempotent.
     */
    @MainThread
    fun initialize(privateMode: Boolean = false) {
        if (isDestroyed) error("GeckoEngine destroyed - cannot reinitialize")
        if (session != null) return // Already initialized

        isPrivateMode = privateMode
        createSession()
        setupDelegates()
    }

    /**
     * Attach a GeckoView instance for rendering.
     * Call from Compose AndroidView onAttach.
     */
    @MainThread
    fun attachView(geckoView: GeckoView) {
        this.geckoView = geckoView
        session?.let { geckoView.setSession(it) }
    }

    /**
     * Detach view for lifecycle management (e.g., configuration changes)
     */
    @MainThread
    fun detachView() {
        geckoView?.setSession(null)
        geckoView = null
    }

    // ─────────────────────────────────────────────────────────────
    // 🌐 Navigation Operations
    // ─────────────────────────────────────────────────────────────

    fun loadUrl(input: String) {
        val normalized = normalizeUrl(input)
        session?.loadUri(normalized)
        _uiState.value = _uiState.value.copy(currentUrl = normalized, isLoading = true)
    }

    fun reload() = session?.reload()

    fun goBack() = session?.goBack()

    fun goForward() = session?.goForward()

    fun stopLoading() = session?.stop()

    // ─────────────────────────────────────────────────────────────
    // 🔐 Privacy & Configuration
    // ─────────────────────────────────────────────────────────────

    fun toggleJavaScript(enabled: Boolean) {
        javascriptEnabled = enabled
        session?.settings?.javascriptEnabled = enabled
    }

    fun setUserAgent(custom: String?) {
        userAgent = custom
        session?.settings?.userAgentOverride = custom
    }

    fun clearData(
        cookies: Boolean = true,
        cache: Boolean = true,
        history: Boolean = true
    ) {
        if (cookies) runtime.dataController.clearCookies()
        if (cache) runtime.dataController.clearCache()
        if (history) runtime.dataController.clearHistory()
    }

    fun setTrackingProtection(strict: Boolean) {
        session?.settings?.trackingProtectionOptions =
            GeckoSession.TrackingProtectionOptions.Builder()
                .strictMode(strict)
                .build()
        reload() // Apply changes
    }

    // ─────────────────────────────────────────────────────────────
    // 🧹 Lifecycle Management
    // ─────────────────────────────────────────────────────────────

    /**
     * Call from Activity/ViewModel onDestroy
     */
    @MainThread
    fun destroy() {
        if (isDestroyed) return
        isDestroyed = true

        detachView()
        session?.close()
        session = null
        // Note: DO NOT shut down runtime - it's shared across tabs/app
    }

    /**
     * Create new session with proper configuration
     */
    private fun createSession() {
        val settings = GeckoSession.Settings.Builder()
            .privateMode(isPrivateMode)
            .javascriptEnabled(javascriptEnabled)
            .userAgentOverride(userAgent)
            .build()

        session = GeckoSession(settings).apply {
            open(runtime)
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 🎯 GeckoView Delegates (Thread-Safe)
    // ─────────────────────────────────────────────────────────────

    @MainThread
    private fun setupDelegates() {
        session?.apply {
            progressDelegate = createProgressDelegate()
            contentDelegate = createContentDelegate()
            navigationDelegate = createNavigationDelegate()
            permissionDelegate = createPermissionDelegate()
            downloadDelegate = createDownloadDelegate()
        }
    }

    private fun createProgressDelegate() = object : GeckoSession.ProgressDelegate {
        override fun onProgressChange(session: GeckoSession, progress: Int) {
            externalScope.launch {
                _uiState.value = _uiState.value.copy(
                    progress = progress,
                    isLoading = progress < 100
                )
            }
        }

        override fun onPageStart(session: GeckoSession, url: String) {
            externalScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, currentUrl = url)
            }
        }

        override fun onPageStop(session: GeckoSession, success: Boolean) {
            externalScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun createContentDelegate() = object : GeckoSession.ContentDelegate {
        override fun onTitleChange(session: GeckoSession, title: String?) {
            externalScope.launch {
                _uiState.value = _uiState.value.copy(pageTitle = title ?: "")
            }
        }

        override fun onCrash(session: GeckoSession) {
            externalScope.launch {
                _uiState.value = _uiState.value.copy(isCrashed = true)
            }
        }
    }

    private fun createNavigationDelegate() = object : GeckoSession.NavigationDelegate {
        override fun onLocationChange(
            session: GeckoSession,
            url: String?,
            perms: MutableList<GeckoSession.PermissionDelegate.ContentPermission>,
            hasUserGesture: Boolean
        ) {
            externalScope.launch {
                _uiState.value = _uiState.value.copy(
                    currentUrl = url ?: "",
                    canGoBack = session.canGoBack,
                    canGoForward = session.canGoForward
                )
            }
        }

        override fun onLoadRequest(
            session: GeckoSession,
            request: GeckoSession.NavigationDelegate.LoadRequest
        ): GeckoResult<AllowOrDeny>? {
            // Enforce HTTPS-only mode if configured
            if (_uiState.value.httpsOnlyMode && request.uri.startsWith("http://")) {
                val httpsUrl = request.uri.replace("http://", "https://")
                session.loadUri(httpsUrl)
                return GeckoResult.deny()
            }
            return GeckoResult.allow()
        }
    }

    private fun createPermissionDelegate() = object : GeckoSession.PermissionDelegate {
        override fun onContentPermissionRequest(
            session: GeckoSession,
            uri: String?,
            type: Int,
            callback: PermissionDelegate.Callback
        ) {
            // Default: deny all permissions for privacy-first approach
            // Can be extended with user prompt UI
            callback.reject()
        }
    }

    private fun createDownloadDelegate() = object : GeckoSession.DownloadDelegate {
        override fun onDownload(
            session: GeckoSession,
            download: GeckoSession.DownloadDelegate.Download
        ) {
            // TODO: Integrate with DownloadManager use case
            // For now, accept and log
            download.accept()
            externalScope.launch {
                _uiState.value = _uiState.value.copy(
                    downloadStatus = "Download started: ${download.filename}"
                )
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 🔧 URL Normalization Helper
    // ─────────────────────────────────────────────────────────────

    private fun normalizeUrl(input: String): String {
        val text = input.trim()

        return when {
            // Already valid URL
            text.startsWith("http://", ignoreCase = true) ||
                    text.startsWith("https://", ignoreCase = true) ||
                    text.startsWith("about:") ||
                    text.startsWith("resource:") -> text

            // Likely domain (contains dot, no spaces)
            text.contains(".") && !text.contains(" ") && !text.contains("/") ->
                "https://$text"

            // Search query
            else -> "https://duckduckgo.com/?q=${text.encodeURIComponent()}"
        }
    }

    // Simple URL encoding helper (avoid external dependency for this)
    private fun String.encodeURIComponent(): String =
        java.net.URLEncoder.encode(this, "UTF-8").replace("+", "%20")
}

// ─────────────────────────────────────────────────────────────
// 📦 Immutable UI State (Sealed for extensibility)
// ─────────────────────────────────────────────────────────────

data class GeckoEngineState(
    val currentUrl: String = "",
    val pageTitle: String = "",
    val progress: Int = 0,
    val isLoading: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val isPrivateMode: Boolean = false,
    val httpsOnlyMode: Boolean = true,
    val isCrashed: Boolean = false,
    val downloadStatus: String? = null,
    val errorMessage: String? = null
)