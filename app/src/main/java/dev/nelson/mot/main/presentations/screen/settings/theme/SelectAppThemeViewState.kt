package dev.nelson.mot.main.presentations.screen.settings.theme

import dev.nelson.mot.core.ui.model.MotAppTheme

data class SelectAppThemeViewState(
    val appThemeList: List<MotAppTheme> = MotAppTheme.getThemes(),
    val selectedAppTheme: MotAppTheme = MotAppTheme.default
)
