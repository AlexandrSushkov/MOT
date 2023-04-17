package dev.nelson.mot.main.presentations.screen.settings

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.preferences.MotSwitch
import dev.nelson.mot.main.domain.use_case.settings.ExportDataBaseUseCase
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.settings.GetSwitchStatusUseCase
import dev.nelson.mot.main.domain.use_case.settings.ImportDataBaseParams
import dev.nelson.mot.main.domain.use_case.settings.ImportDataBaseUseCase
import dev.nelson.mot.main.domain.use_case.settings.SetSwitchStatusParams
import dev.nelson.mot.main.domain.use_case.settings.SetSwitchStatusUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSwitchStatusUseCase: GetSwitchStatusUseCase,
    private val exportDataBaseUseCase: ExportDataBaseUseCase,
    private val importDataBaseUseCase: ImportDataBaseUseCase,
    private val setSwitchStatusUseCase: SetSwitchStatusUseCase
) : BaseViewModel() {

    // actions
    val restartAppAction
        get() = _restartAppAction.asSharedFlow()
    private val _restartAppAction = MutableSharedFlow<Unit>()


    // states
    val darkThemeSwitchState
        get() = _darkTheme.asStateFlow()
    private val _darkTheme = MutableStateFlow(false)

    val dynamicColorThemeSwitchState
        get() = _dynamicColorTheme.asStateFlow()
    private val _dynamicColorTheme = MutableStateFlow(false)

    val showAlertDialogState
        get() = _showAlertDialogState.asStateFlow()
    private val _showAlertDialogState = MutableStateFlow<Pair<Boolean, AlertDialogParams?>>(false to null)

    init {
        launch {
            getSwitchStatusUseCase.execute(MotSwitch.DarkTheme)
                .collect { _darkTheme.value = it }
        }

        launch {
            getSwitchStatusUseCase.execute(MotSwitch.DynamicColorTheme)
                .collect { _dynamicColorTheme.value = it }
        }
    }

    /**
     * only one them can be set at the time
     */
    fun onDarkThemeCheckedChange(isChecked: Boolean) = launch {
        if (isChecked) {
            val params = SetSwitchStatusParams(MotSwitch.DynamicColorTheme, false) // force turn off dynamic color theme
            setSwitchStatusUseCase.execute(params)
        }
        val params = SetSwitchStatusParams(MotSwitch.DarkTheme, isChecked)
        setSwitchStatusUseCase.execute(params)
    }

    /**
     * only one them can be set at the time
     */
    fun onDynamicColorThemeCheckedChange(isChecked: Boolean) = launch {
        if (isChecked) {
            val params = SetSwitchStatusParams(MotSwitch.DarkTheme, false) // force turn off dark theme
            setSwitchStatusUseCase.execute(params)
        }
        val params = SetSwitchStatusParams(MotSwitch.DynamicColorTheme, isChecked)
        setSwitchStatusUseCase.execute(params)
    }

    fun onExportDataBaseClick() = launch {
        runCatching { exportDataBaseUseCase.execute() }
            .onFailure {
                Timber.e(it.message) // TODO: 2021-09-10 handle error
            }
            .onSuccess { isExported ->
                val toastMessage = if (isExported) DATA_BASE_EXPORTED_SUCCESSFULLY else DATA_BASE_EXPORT_FAILED
                showToast(toastMessage)
                Timber.d(toastMessage)
            }
    }

    fun onImportDataBaseEvent(uri: Uri) = launch {
        val alertDialogParams = AlertDialogParams(
            message = "Are you sure you want to import this data base?",
            onPositiveClickCallback = { importDataBase(uri)},
            onNegativeClickCallback = { hideAlertDialog() }
        )
        _showAlertDialogState.emit(true to alertDialogParams)
    }

    private fun importDataBase(uri: Uri) = launch {
        runCatching { importDataBaseUseCase.execute(ImportDataBaseParams(uri)) }
            .onFailure {
                // TODO: 2021-09-10 handle error
            }
            .onSuccess { _restartAppAction.emit(Unit) }
    }
    private fun hideAlertDialog() = launch {
        _showAlertDialogState.emit(false to null)
    }

    fun onRequestPermission() {
        TODO("Not yet implemented")
    }

    companion object {
        const val DATA_BASE_EXPORTED_SUCCESSFULLY = "data base was exported successfully. Please, check Downloads folder."
        const val DATA_BASE_EXPORT_FAILED = "Oops! data base export failed."
    }
}

data class AlertDialogParams(
    val message: String,
    val onPositiveClickCallback: () -> Unit,
    val onNegativeClickCallback: () -> Unit = {},
)
