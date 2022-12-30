package dev.nelson.mot.main.data.preferences

import androidx.datastore.preferences.core.Preferences

sealed class MotSwitch(val key: Preferences.Key<Boolean>) {

    object DarkTheme : MotSwitch(PreferencesKeys.DARK_THEME_ENABLED)
    object DynamicColorTheme : MotSwitch(PreferencesKeys.DYNAMIC_COLOR_THEME_ENABLED)

}
