package dev.nelson.mot.main.presentations.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.preferences.MotSwitchType
import dev.nelson.mot.main.domain.use_case.settings.GetSwitchStatusUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val getSwitchStatusUseCase: GetSwitchStatusUseCase
) : BaseViewModel() {

    val isLoading
        get() = _isLoading.asStateFlow()
    private val _isLoading = MutableStateFlow(true)

    val darkThemeEnabled
        get() = _forceDark.asStateFlow()
    private val _forceDark = MutableStateFlow(false)

    val dynamicColorEnabled
        get() = _dynamicColorEnabled.asStateFlow()
    private val _dynamicColorEnabled = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            // do something
//            delay(1000)
            _isLoading.value = false
        }

        viewModelScope.launch {
            getSwitchStatusUseCase.execute(MotSwitchType.DarkTheme)
                .collect { _forceDark.value = it }
        }

        viewModelScope.launch {
            getSwitchStatusUseCase.execute(MotSwitchType.DynamicColorTheme)
                .collect { _dynamicColorEnabled.value = it }
        }
    }
}
