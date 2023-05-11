package dev.nelson.mot.main.presentations.screen.settings

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.preferences.MotSwitchType
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.settings.ExportDataBaseUseCase
import dev.nelson.mot.main.domain.use_case.settings.GetSwitchStatusUseCase
import dev.nelson.mot.main.domain.use_case.settings.ImportDataBaseUseCase
import dev.nelson.mot.main.domain.use_case.settings.SetSwitchStatusParams
import dev.nelson.mot.main.domain.use_case.settings.SetSwitchStatusUseCase
import dev.nelson.mot.main.presentations.AlertDialogParams
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

    val showPermissionDialogAction
        get() = _showPermissionDialogAction.asSharedFlow()
    private val _showPermissionDialogAction = MutableSharedFlow<Unit>()

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
            getSwitchStatusUseCase.execute(MotSwitchType.DarkTheme)
                .collect { _darkTheme.value = it }
        }

        launch {
            getSwitchStatusUseCase.execute(MotSwitchType.DynamicColorTheme)
                .collect { _dynamicColorTheme.value = it }
        }
    }

    /**
     * Only one them can be set at the time
     */
    fun onDarkThemeCheckedChange(isChecked: Boolean) = launch {
        if (isChecked) {
            val params = SetSwitchStatusParams(MotSwitchType.DynamicColorTheme, false) // force turn off dynamic color theme
            setSwitchStatusUseCase.execute(params)
        }
        val params = SetSwitchStatusParams(MotSwitchType.DarkTheme, isChecked)
        setSwitchStatusUseCase.execute(params)
    }

    /**
     * Only one them can be set at the time
     */
    fun onDynamicColorThemeCheckedChange(isChecked: Boolean) = launch {
        if (isChecked) {
            val params = SetSwitchStatusParams(MotSwitchType.DarkTheme, false) // force turn off dark theme
            setSwitchStatusUseCase.execute(params)
        }
        val params = SetSwitchStatusParams(MotSwitchType.DynamicColorTheme, isChecked)
        setSwitchStatusUseCase.execute(params)
    }

    fun onExportDataBaseClick() = launch {
        runCatching { exportDataBaseUseCase.execute() }
            .onSuccess { isExported ->
                val message = if (isExported) R.string.database_exported_successfully_dialog_message else R.string.database_export_failed_dialog_message
                val alertDialogParams = AlertDialogParams(
                    message = message,
                    dismissClickCallback = { hideAlertDialog() },
                    onPositiveClickCallback = { hideAlertDialog() },
                )
                _showAlertDialogState.emit(true to alertDialogParams)
            }.onFailure { throwable ->
                throwable.message?.let {
                    showToast(it)
                    Timber.e(it)
                }
            }
    }

    fun onImportDataBaseEvent(uri: Uri) = launch {
        val alertDialogParams = AlertDialogParams(
            message = R.string.import_database_dialog_message,
            dismissClickCallback = { hideAlertDialog() },
            onPositiveClickCallback = {
                hideAlertDialog()
                importDataBase(uri)
            },
            onNegativeClickCallback = { hideAlertDialog() }
        )
        _showAlertDialogState.emit(true to alertDialogParams)
    }

    private fun importDataBase(uri: Uri) = launch {
        runCatching { importDataBaseUseCase.execute(uri) }
            .onSuccess { isImported ->
                if (isImported) {
                    _restartAppAction.emit(Unit)
                } else {
                    showToast("Oops! data base import failed.")
                }
            }
            .onFailure { throwable ->
                throwable.message?.let {
                    showToast(it)
                    Timber.e(it)
                }
            }
    }

    private fun hideAlertDialog() = launch {
        _showAlertDialogState.emit(false to null)
    }
}
