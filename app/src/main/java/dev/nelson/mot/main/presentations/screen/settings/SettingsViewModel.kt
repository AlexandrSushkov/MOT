package dev.nelson.mot.main.presentations.screen.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.preferences.MotSwitch
import dev.nelson.mot.main.domain.use_case.ExportDataBaseUseCase
import dev.nelson.mot.main.domain.use_case.settings.GetSwitchStatusUseCase
import dev.nelson.mot.main.domain.use_case.settings.SetSwitchStatusUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exportDataBaseUseCase: ExportDataBaseUseCase,
    private val getSwitchStatusUseCase: GetSwitchStatusUseCase,
    private val setSwitchStatusUseCase: SetSwitchStatusUseCase
) : BaseViewModel() {

    val darkThemeSwitchState
        get() = _darkTheme.asStateFlow()
    private val _darkTheme = MutableStateFlow(false)

    val colorThemeSwitchState
        get() = _colorTheme.asStateFlow()
    private val _colorTheme = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            getSwitchStatusUseCase.execute(MotSwitch.DarkTheme)
                .collect { _darkTheme.value = it }
        }

        viewModelScope.launch {
            getSwitchStatusUseCase.execute(MotSwitch.DynamicColorTheme)
                .collect { _colorTheme.value = it }
        }
    }

    /**
     * only one them can be set at the time
     */
    fun onDarkThemeCheckedChange(isChecked: Boolean) {
        viewModelScope.launch {
            if (isChecked) {
                setSwitchStatusUseCase.execute(MotSwitch.DynamicColorTheme, false) // force turn off dynamic color theme
            }
            setSwitchStatusUseCase.execute(MotSwitch.DarkTheme, isChecked)
        }
    }

    /**
     * only one them can be set at the time
     */
    fun onDynamicColorThemeCheckedChange(isChecked: Boolean) {
        viewModelScope.launch {
            if (isChecked) {
                setSwitchStatusUseCase.execute(MotSwitch.DarkTheme, false) // force turn off dark theme
            }
            setSwitchStatusUseCase.execute(MotSwitch.DynamicColorTheme, isChecked)
        }
    }

    fun onExportDataBaseClick() {
        viewModelScope.launch {
            val isExported = exportDataBaseUseCase.execute()
            val toastMessage = if (isExported) DATA_BASE_EXPORTED_SUCCESSFULLY else DATA_BASE_EXPORT_FAILED
            showToast(toastMessage)
        }
    }


    companion object {
        const val DATA_BASE_EXPORTED_SUCCESSFULLY = "data base was exported successfully. Please, check Downloads folder."
        const val DATA_BASE_EXPORT_FAILED = "Oops! data base export failed."
    }
}
