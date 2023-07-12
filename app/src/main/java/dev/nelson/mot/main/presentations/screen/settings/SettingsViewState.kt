package dev.nelson.mot.main.presentations.screen.settings

import dev.nelson.mot.core.ui.model.MotAppTheme
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.presentations.AlertDialogParams
import java.util.Locale

data class SettingsViewState(
    val selectedAppTheme: MotAppTheme = MotAppTheme.default,
    val isDynamicThemeSwitchChecked: Boolean= false,
    val isShowCentsSwitchChecked: Boolean = false,
    val isShowCurrencySymbolSwitchChecked: Boolean = false,
    val isShowDigitsSwitchChecked: Boolean = false,
    val isShowCountryPicker: Boolean = false,
    val selectedLocale: Locale = Locale.getDefault(),
    val countries: List<Locale> = emptyList(),
    val alertDialog: AlertDialogParams? = null,
    val priceViewState: PriceViewState = PriceViewState()
)
