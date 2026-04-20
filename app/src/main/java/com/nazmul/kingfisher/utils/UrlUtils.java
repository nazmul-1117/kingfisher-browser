package com.nazmul.kingfisher.utils;

import android.webkit.URLUtil;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * URL + Search handler
 */
public final class UrlUtils {
    private UrlUtils() {}

    public static String normalizeInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Constants.DEFAULT_HOME_URL;
        }

        String text = input.trim();

        // real URL
        if (isValidUrl(text)) {
            return fixUrl(text);
        }

        // search query (always safe encoding)
        return buildDuckDuckGoSearch(text);
    }

    private static boolean isValidUrl(String input) {
        return URLUtil.isValidUrl(input)
                || input.startsWith("http://")
                || input.startsWith("https://");
    }

    private static String fixUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "https://" + url;
        }
        return url;
    }

    private static String buildDuckDuckGoSearch(String query) {
        try {
            String encoded = URLEncoder.encode(query, "UTF-8");
            return "https://duckduckgo.com/?q=" + encoded;
        } catch (Exception e) {
            return "https://duckduckgo.com/?q=" + query;
        }
    }

    public static boolean isSafeScheme(String url) {
        return url != null &&
                (url.startsWith("http://") || url.startsWith("https://"));
    }
}