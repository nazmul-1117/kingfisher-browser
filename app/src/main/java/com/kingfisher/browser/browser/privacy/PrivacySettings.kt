package com.kingfisher.browser.browser.privacy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class PrivacySettings(
    val blockAds: Boolean = true,
    val blockTrackers: Boolean = true,
    val httpsOnlyMode: Boolean = true,
    val blockCookies: Boolean = false,
    val incognitoMode: Boolean = false
)

@Singleton
class PrivacySettingsManager @Inject constructor() {

    private val _settings = MutableStateFlow(PrivacySettings())
    val settings: StateFlow<PrivacySettings> = _settings

    fun update(update: PrivacySettings) {
        _settings.value = update
    }

    fun updateBlockAds(value: Boolean) {
        _settings.value = _settings.value.copy(blockAds = value)
    }

    fun updateTrackers(value: Boolean) {
        _settings.value = _settings.value.copy(blockTrackers = value)
    }

    fun setIncognito(value: Boolean) {
        _settings.value = _settings.value.copy(incognitoMode = value)
    }
}