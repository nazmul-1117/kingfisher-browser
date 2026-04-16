package com.nazmul.kingfisher.web;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.nazmul.kingfisher.utils.UrlUtils;

/**
 * Encapsulates WebView initialization, security hardening, and navigation logic.
 * Keeps MainActivity thin and testable.
 */
public class BrowserEngine {
    private final WebView webView;

    public BrowserEngine(Context context, WebView webView) {
        this.webView = webView;
        initializeWebView(context.getApplicationContext());
    }

    private void initializeWebView(Context context) {
        WebSettings settings = webView.getSettings();

        // 🔐 Security Hardening
        settings.setJavaScriptEnabled(true); // Required for modern web apps
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setSafeBrowsingEnabled(true); // Blocks phishing/malware
        }

        // 🛡️ Privacy: Block third-party tracking cookies
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(webView, false);

        // 🧭 Attach secure navigation handler
        webView.setWebViewClient(new SecureWebViewClient());

        // 🎨 UX Polish
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    }

    /**
     * Loads a URL with built-in normalization & safety checks.
     */
    public void loadUrl(String url) {
        String safeUrl = UrlUtils.normalizeUrl(url);
        webView.loadUrl(safeUrl);
    }

    public boolean canGoBack() {
        return webView.canGoBack();
    }

    public void goBack() {
        if (canGoBack()) {
            webView.goBack();
        }
    }

    public void destroy() {
        webView.removeAllViews();
        webView.destroy();
    }
}