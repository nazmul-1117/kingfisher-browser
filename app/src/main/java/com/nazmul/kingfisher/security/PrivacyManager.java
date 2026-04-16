package com.nazmul.kingfisher.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.CookieManager;
import android.webkit.WebStorage;
import android.webkit.WebView;

/**
 * Handles incognito state, cookie clearing, and tracking prevention.
 * Uses SharedPreferences for fast, lightweight state persistence.
 */
public class PrivacyManager {
    private static final String PREFS_NAME = "kingfisher_privacy";
    private static final String KEY_INCOGNITO = "incognito_mode";

    private final SharedPreferences prefs;
    private final CookieManager cookieManager;

    public PrivacyManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.cookieManager = CookieManager.getInstance();
    }

    public boolean isIncognito() {
        return prefs.getBoolean(KEY_INCOGNITO, false);
    }

    public void setIncognito(boolean enabled) {
        prefs.edit().putBoolean(KEY_INCOGNITO, enabled).apply();
        if (enabled) {
            clearAllBrowsingData();
        }
    }

    /**
     * Clears cookies, cache, and DOM storage. Must be called when exiting incognito.
     */
    public void clearAllBrowsingData() {
        cookieManager.removeAllCookies(null);
        cookieManager.flush();
        WebStorage.getInstance().deleteAllData();
        // Note: WebView.clearCache() requires a WebView instance; handled in BrowserEngine
    }

    /**
     * Clears WebView-specific cache. Call during onDestroy or mode toggle.
     */
    public void clearWebViewCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }
}