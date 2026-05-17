package com.kingfisher.browser.browser.privacy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlocklistRepository @Inject constructor() {

    private val defaultBlockedDomains = setOf(
        "doubleclick.net",
        "googlesyndication.com",
        "adservice.google.com",
        "facebook.com/tr",
        "ads.twitter.com",
        "analytics.google.com"
    )

    private val _blocklist = MutableStateFlow(defaultBlockedDomains)
    val blocklist: StateFlow<Set<String>> = _blocklist

    fun isBlocked(url: String): Boolean {
        return _blocklist.value.any { domain ->
            url.contains(domain, ignoreCase = true)
        }
    }

    fun addDomain(domain: String) {
        _blocklist.value = _blocklist.value + domain
    }

    fun removeDomain(domain: String) {
        _blocklist.value = _blocklist.value - domain
    }
}