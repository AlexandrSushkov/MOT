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

    // states
    private val defaultSettingsViewState = SettingsViewState(
        isDarkThemeSwitchOn = false,
        isDynamicThemeSwitchOn = false
    )
    val settingsViewState
        get() = _viewState.asStateFlow()
    private val _viewState = MutableStateFlow(defaultSettingsViewState)

    init {
        launch {
            getSwitchStatusUseCase.execute(MotSwitchType.DarkTheme).collect {
                _viewState.value = _viewState.value.copy(isDarkThemeSwitchOn = it)
            }
        }

        launch {
            getSwitchStatusUseCase.execute(MotSwitchType.DynamicColorTheme).collect {
                _viewState.value = _viewState.value.copy(isDynamicThemeSwitchOn = it)
            }
        }
    }

    /**
     * Only one them can be set at the time
     */
    fun onDarkThemeCheckedChange(isChecked: Boolean) = launch {
        if (isChecked) {
            resetSwitch(MotSwitchType.DynamicColorTheme)
        }
        val params = SetSwitchStatusParams(MotSwitchType.DarkTheme, isChecked)
        setSwitchStatusUseCase.execute(params)
    }

    /**
     * Only one them can be set at the time
     */
    fun onDynamicColorThemeCheckedChange(isChecked: Boolean) = launch {
        if (isChecked) {
            resetSwitch(MotSwitchType.DarkTheme)
        }
        val params = SetSwitchStatusParams(MotSwitchType.DynamicColorTheme, isChecked)
        setSwitchStatusUseCase.execute(params)
    }

    fun onExportDataBaseClick() = launch {
        runCatching { exportDataBaseUseCase.execute() }.onSuccess { isExported ->
            val message = if (isExported) {
                R.string.database_exported_successfully_dialog_message
            } else {
                R.string.database_export_failed_dialog_message
            }
            val alertDialogParams = getExportDataBaseDialog(message)
            _viewState.value = _viewState.value.copy(alertDialog = alertDialogParams)
        }.onFailure { throwable ->
            throwable.message?.let {
                showToast(it)
                Timber.e(it)
            }
        }
    }

    fun onImportDataBaseEvent(uri: Uri) = launch {
        val alertDialogParams = getImportDataBaseDialog(uri)
        _viewState.value = _viewState.value.copy(alertDialog = alertDialogParams)
    }

    private fun getExportDataBaseDialog(message: Int): AlertDialogParams {
        return AlertDialogParams(
            message = message,
            dismissClickCallback = { hideAlertDialog() },
            onPositiveClickCallback = { hideAlertDialog() },
        )
    }

    private fun getImportDataBaseDialog(uri: Uri): AlertDialogParams {
        return AlertDialogParams(message = R.string.import_database_dialog_message,
            dismissClickCallback = { hideAlertDialog() },
            onPositiveClickCallback = {
                hideAlertDialog()
                importDataBase(uri)
            },
            onNegativeClickCallback = { hideAlertDialog() })
    }

    private fun importDataBase(uri: Uri) = launch {
        runCatching { importDataBaseUseCase.execute(uri) }
            .onSuccess { isImported ->
                if (isImported) {
                    _restartAppAction.emit(Unit)
                } else {
                    showToast("Oops! data base import failed.")
                }
            }.onFailure { throwable ->
                throwable.message?.let {
                    showToast(it)
                    Timber.e(it)
                }
            }
    }

    /**
     * Force turn off switch.
     */
    private suspend fun resetSwitch(motSwitchType: MotSwitchType) {
        val params = SetSwitchStatusParams(motSwitchType, false)
        setSwitchStatusUseCase.execute(params)
    }

    private fun hideAlertDialog() = launch {
        _viewState.value = _viewState.value.copy(
            alertDialog = null
        )
    }
}
