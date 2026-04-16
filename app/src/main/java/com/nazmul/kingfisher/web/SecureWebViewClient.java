package com.nazmul.kingfisher.web;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.nazmul.kingfisher.utils.UrlUtils;

/**
 * Handles navigation interception and security policies.
 * Uses modern WebResourceRequest (API 24+) to avoid deprecated methods.
 */
public class SecureWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();

        // Block unsafe schemes (file://, javascript://, intent://, etc.)
        if (!UrlUtils.isSafeScheme(url)) {
            return true; // Block navigation
        }

        return false; // Let WebView handle it normally
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        // TODO: Hook for UI loading indicator & address bar sync
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // TODO: Hook for page title, history, bookmark UI updates
    }
}