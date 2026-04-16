package com.nazmul.kingfisher.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.nazmul.kingfisher.utils.UrlUtils;

public class BrowserEngine {
    private final WebView webView;
    private BrowserEngineCallback callback;

    /** Callback interface to keep MainActivity thin and decoupled */
    public interface BrowserEngineCallback {
        void onUrlChanged(String url);
        void onProgressChanged(int progress);
        void onLoadingStateChanged(boolean isLoading);
        void onNavStateChanged(boolean canBack, boolean canForward);
    }

    public BrowserEngine(Context context, WebView webView) {
        this.webView = webView;
        initializeWebView(context.getApplicationContext());
    }

    public void setCallback(BrowserEngineCallback callback) {
        this.callback = callback;
    }

    private void initializeWebView(Context context) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setSafeBrowsingEnabled(true);
        }

        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, false);

        // 🔒 Security + UI State Callbacks
        webView.setWebViewClient(new SecureWebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (callback != null) {
                    callback.onLoadingStateChanged(true);
                    callback.onProgressChanged(0);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (callback != null) {
                    callback.onUrlChanged(url);
                    callback.onLoadingStateChanged(false);
                    callback.onProgressChanged(100);
                    callback.onNavStateChanged(view.canGoBack(), view.canGoForward());
                }
            }
        });

        // 📊 Progress Tracking
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (callback != null) {
                    callback.onProgressChanged(newProgress);
                }
            }
        });
    }

    public void loadUrl(String url) {
        String safeUrl = UrlUtils.normalizeUrl(url);
        webView.loadUrl(safeUrl);
    }

    public void goBack() { if (webView.canGoBack()) webView.goBack(); }
    public void goForward() { if (webView.canGoForward()) webView.goForward(); }
    public void reload() { webView.reload(); }
    public void stopLoading() { webView.stopLoading(); }

    public boolean canGoBack() { return webView.canGoBack(); }
    public boolean canGoForward() { return webView.canGoForward(); }
    public String getCurrentUrl() { return webView.getUrl(); }

    public void destroy() {
        webView.removeAllViews();
        webView.destroy();
    }
}