package dev.nelson.mot.main.presentations.screen.settings

import dev.nelson.mot.main.presentations.AlertDialogParams
import java.util.Locale

data class SettingsViewState(
    val isDarkThemeSwitchOn: Boolean = false,
    val isDynamicThemeSwitchOn: Boolean= false,
    val isShowCents: Boolean = false,
    val isShowCurrencySymbol: Boolean = false,
    val selectedLocale: Locale = Locale.getDefault(),
    val alertDialog: AlertDialogParams? = null,
)
