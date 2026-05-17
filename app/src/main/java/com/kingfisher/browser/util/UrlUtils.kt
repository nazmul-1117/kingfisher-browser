package com.kingfisher.browser.util

object UrlUtils {

    fun normalize(input: String): String {

        val trimmed = input.trim()

        // If already a full URL
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed
        }

        // If looks like domain (google.com)
        if (trimmed.contains(".") && !trimmed.contains(" ")) {
            return "https://$trimmed"
        }

        // Otherwise treat as search
        return "https://www.google.com/search?q=" +
                trimmed.replace(" ", "+")
    }

    fun isUrl(input: String): Boolean {
        return input.contains(".") &&
                !input.contains(" ")
    }
}