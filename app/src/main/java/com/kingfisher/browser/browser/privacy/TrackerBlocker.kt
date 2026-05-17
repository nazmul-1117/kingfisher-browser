package com.kingfisher.browser.browser.privacy

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerBlocker @Inject constructor(
    private val blocklistRepository: BlocklistRepository
) {

    fun shouldBlock(url: String): Boolean {
        return blocklistRepository.isBlocked(url)
    }

    fun isTracker(url: String): Boolean {
        val trackers = listOf(
            "google-analytics",
            "doubleclick",
            "facebook.com/tr",
            "ads",
            "tracker"
        )

        return trackers.any { url.contains(it, ignoreCase = true) }
    }

    fun isBlocked(url: String): Boolean {
        return shouldBlock(url) || isTracker(url)
    }
}