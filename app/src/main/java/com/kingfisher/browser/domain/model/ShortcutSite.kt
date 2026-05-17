package com.kingfisher.browser.domain.model

data class ShortcutSite(
    val id: Long = 0,
    val name: String,
    val url: String,
    val faviconUrl: String = "https://www.google.com/s2/favicons?sz=64&domain_url=$url"
)