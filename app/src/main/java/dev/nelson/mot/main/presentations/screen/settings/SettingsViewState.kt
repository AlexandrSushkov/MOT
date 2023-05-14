package dev.nelson.mot.main.presentations.screen.settings

import dev.nelson.mot.main.presentations.AlertDialogParams

data class SettingsViewState(
    val isDarkThemeSwitchOn: Boolean = false,
    val isDynamicThemeSwitchOn: Boolean= false,
    val isShowCents: Boolean = false,
    val alertDialog: AlertDialogParams? = null,
)
