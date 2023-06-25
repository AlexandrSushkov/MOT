package dev.nelson.mot.main.presentations.screen.home

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.core.ui.view_state.AppThemeViewState
import dev.nelson.mot.main.data.preferences.MotSwitchType
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.settings.GetAppThemeUseCase
import dev.nelson.mot.main.domain.use_case.settings.GetSwitchStatusUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MotThemeViewModel @Inject constructor(
    getAppThemeUseCase: GetAppThemeUseCase,
    getSwitchStatusUseCase: GetSwitchStatusUseCase
) : BaseViewModel() {

    val appThemeViewState
        get() = _appThemeViewState.asStateFlow()
    private val _appThemeViewState = MutableStateFlow(AppThemeViewState())

    init {
        launch {
            getAppThemeUseCase.execute()
                .collect {
                    _appThemeViewState.value =
                        _appThemeViewState.value.copy(selectedTheme = it)
                }
        }

        launch {
            getSwitchStatusUseCase.execute(MotSwitchType.DynamicColorTheme)
                .collect {
                    _appThemeViewState.value =
                        _appThemeViewState.value.copy(dynamicColorThemeEnabled = it)
                }
        }
    }
}
