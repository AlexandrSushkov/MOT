package dev.nelson.mot.core.ui.view_state

data class AppThemeViewState(
    val forceDarkThemeEnabled: Boolean = false,
    val dynamicColorThemeEnabled: Boolean = false // Dynamic color is available on Android 12+
)
