package dev.nelson.mot.main.presentations.screen.home

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.core.ui.view_state.AppThemeViewState
import dev.nelson.mot.main.data.preferences.MotSwitchType
import dev.nelson.mot.main.domain.use_case.settings.GetSwitchStatusUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Basically is it application viewState. app is single activity + composable UI
 */
@HiltViewModel
class SplashScreenViewModel @Inject constructor() : BaseViewModel() {

    val isLoading
        get() = _isLoading.asStateFlow()
    private val _isLoading = MutableStateFlow(true)

    init {
        launch {
            // do something
//            delay(1000)
//            _isLoading.value = true
        }
    }

    fun onRemoteConfigFetched() {
        _isLoading.value = false
    }
}
