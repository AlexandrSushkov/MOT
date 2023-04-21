package dev.nelson.mot.main.data.preferences

import androidx.datastore.preferences.core.Preferences

sealed class MotSwitchType(val key: Preferences.Key<Boolean>) {

    object DarkTheme : MotSwitchType(PreferencesKeys.DARK_THEME_ENABLED)
    object DynamicColorTheme : MotSwitchType(PreferencesKeys.DYNAMIC_COLOR_THEME_ENABLED)
}
