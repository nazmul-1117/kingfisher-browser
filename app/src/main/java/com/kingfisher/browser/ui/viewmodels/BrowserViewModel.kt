package com.kingfisher.browser.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingfisher.browser.browser.engine.EngineState
import com.kingfisher.browser.browser.engine.GeckoEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    val engine: GeckoEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowserUiState())
    val uiState: StateFlow<BrowserUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            engine.state.collect { engineState ->
                _uiState.update { it.copy(engineState = engineState, currentInput = engineState.currentUrl ?: it.currentInput) }
            }
        }
    }

    fun onUrlSubmit(url: String) {
        _uiState.value = _uiState.value.copy(screenMode = ScreenMode.BROWSER, isLoading = true)
        engine.loadUrl(url)
    }

    fun onNavigateHome() {
        _uiState.update { it.copy(screenMode = ScreenMode.HOME, isLoading = false) }
        engine.stop()
    }

    fun onInputChanged(input: String) {
        _uiState.update { it.copy(currentInput = input) }
    }

    fun onReload() = engine.reload()
    fun onBack() = engine.goBack()
    fun onForward() = engine.goForward()

    override fun onCleared() {
        super.onCleared()
        engine.onDestroy()
    }

    data class BrowserUiState(
        val screenMode: ScreenMode = ScreenMode.HOME,
        val engineState: EngineState = EngineState(),
        val currentInput: String = "",
        val isLoading: Boolean = false
    )

    enum class ScreenMode { HOME, BROWSER }
}