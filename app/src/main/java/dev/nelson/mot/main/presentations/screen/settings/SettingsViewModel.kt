package dev.nelson.mot.main.presentations.screen.settings

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.model.MotAppTheme
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.preferences.MotSwitchType
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.price.GetPriceViewStateUseCase
import dev.nelson.mot.main.domain.use_case.settings.ExportDataBaseUseCase
import dev.nelson.mot.main.domain.use_case.settings.GetAppThemeUseCase
import dev.nelson.mot.main.domain.use_case.settings.GetSelectedLocaleUseCase
import dev.nelson.mot.main.domain.use_case.settings.GetSwitchStatusUseCase
import dev.nelson.mot.main.domain.use_case.settings.ImportDataBaseUseCase
import dev.nelson.mot.main.domain.use_case.settings.RandomizeDataBaseDataUseCase
import dev.nelson.mot.main.domain.use_case.settings.SetSwitchStatusParams
import dev.nelson.mot.main.domain.use_case.settings.SetSwitchStatusUseCase
import dev.nelson.mot.main.presentations.AlertDialogParams
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSwitchStatusUseCase: GetSwitchStatusUseCase,
    getSelectedLocaleUseCase: GetSelectedLocaleUseCase,
    getPriceViewStateUseCase: GetPriceViewStateUseCase,
    getAppThemeUseCase: GetAppThemeUseCase,
    private val exportDataBaseUseCase: ExportDataBaseUseCase,
    private val importDataBaseUseCase: ImportDataBaseUseCase,
    private val setSwitchStatusUseCase: SetSwitchStatusUseCase,
    private val randomizeDataBaseDataUseCase: RandomizeDataBaseDataUseCase,
) : BaseViewModel() {

    // actions
    val restartAppAction
        get() = _restartAppAction.asSharedFlow()
    private val _restartAppAction = MutableSharedFlow<Unit>()

    // states
    val settingsViewState
        get() = _viewState.asStateFlow()
    private val _viewState = MutableStateFlow(SettingsViewState())

    init {
        launch {
            combine(
                getSwitchStatusUseCase.execute(MotSwitchType.DynamicColorTheme),
                getAppThemeUseCase.execute(),
                getSwitchStatusUseCase.execute(MotSwitchType.ShowCents),
                getSwitchStatusUseCase.execute(MotSwitchType.ShowCurrencySymbol),
                getSwitchStatusUseCase.execute(MotSwitchType.ShowDigits),
                getSelectedLocaleUseCase.execute(),
                getPriceViewStateUseCase.execute()
            ) { array ->
                _viewState.value.copy(
                    isDynamicThemeSwitchChecked = array[0] as Boolean,
                    selectedAppTheme = array[1] as MotAppTheme,
                    isShowCentsSwitchChecked = array[2] as Boolean,
                    isShowCurrencySymbolSwitchChecked = array[3] as Boolean,
                    isShowDigitsSwitchChecked = array[4] as Boolean,
                    selectedLocale = array[5] as Locale,
                    priceViewState = array[6] as PriceViewState
                )
            }.collect { _viewState.value = it }
        }
    }

    fun onDynamicColorThemeCheckedChange(isChecked: Boolean) = launch {
        setSwitchStatus(MotSwitchType.DynamicColorTheme, isChecked)
    }

    fun onShowCentsCheckedChange(isChecked: Boolean) = launch {
        setSwitchStatus(MotSwitchType.ShowCents, isChecked)
    }

    fun onShowCurrencySymbolChange(isChecked: Boolean) = launch {
        setSwitchStatus(MotSwitchType.ShowCurrencySymbol, isChecked)
    }

    fun onHideDigitsChange(isChecked: Boolean) = launch {
        setSwitchStatus(MotSwitchType.ShowDigits, isChecked)
    }

    fun onRandomizeDataBaseDataClick() = launch {
        randomizeDataBaseDataUseCase.execute()
        showToast("data was randomized")
    }

    fun onExportDataBaseClick() = launch {
        runCatching { exportDataBaseUseCase.execute() }
            .onSuccess { isExported ->
                val message = if (isExported) {
                    R.string.database_exported_successfully_dialog_message
                } else {
                    R.string.database_export_failed_dialog_message
                }
                val alertDialogParams = getExportDataBaseDialog(message)
                _viewState.update { it.copy(alertDialog = alertDialogParams) }
            }.onFailure { throwable ->
                throwable.message?.let {
                    showToast(it)
                    Timber.e(it)
                }
            }
    }

    fun onImportDataBaseEvent(uri: Uri) = launch {
        val alertDialogParams = getImportDataBaseDialog(uri)
        _viewState.update { it.copy(alertDialog = alertDialogParams) }
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
                    showToast("Oops! database import failed.")
                }
            }.onFailure { throwable ->
                throwable.message?.let {
                    showToast(it)
                    Timber.e(it)
                }
            }
    }

    private suspend fun setSwitchStatus(switchType: MotSwitchType, isChecked: Boolean) {
        val params = SetSwitchStatusParams(switchType, isChecked)
        setSwitchStatusUseCase.execute(params)
    }

    private fun hideAlertDialog() = launch {
        _viewState.update { it.copy(alertDialog = null) }
    }
}
