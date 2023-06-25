package dev.nelson.mot.core.ui.view_state

import dev.nelson.mot.core.ui.model.MotAppTheme

data class AppThemeViewState(
    val selectedTheme: MotAppTheme = MotAppTheme.default,
    val dynamicColorThemeEnabled: Boolean = false // Dynamic color is available on Android 12+
)
