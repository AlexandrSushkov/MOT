package dev.nelson.mot.main.presentations.screen.settings

import dev.nelson.mot.main.presentations.AlertDialogParams

data class SettingsViewState(
    val isDarkThemeSwitchOn: Boolean,
    val isDynamicThemeSwitchOn: Boolean,
    val alertDialog: AlertDialogParams? = null,
)
