package com.nazmul.kingfisher.utils;

import android.webkit.URLUtil;

/**
 * Centralized URL validation & normalization.
 * Enforces HTTPS-first policy and blocks non-web schemes.
 */
public final class UrlUtils {
    private UrlUtils() {}

    /**
     * Normalizes user input: trims whitespace, adds https:// if missing,
     * and falls back to home URL if empty.
     */
    public static String normalizeUrl(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Constants.DEFAULT_HOME_URL;
        }
        String url = input.trim();
        // URLUtil.isValidUrl checks basic syntax
        if (!URLUtil.isValidUrl(url)) {
            url = "https://" + url;
        }
        return url;
    }

    /**
     * Only allow http/https to prevent javascript://, file://, data:// exploits.
     */
    public static boolean isSafeScheme(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }
}