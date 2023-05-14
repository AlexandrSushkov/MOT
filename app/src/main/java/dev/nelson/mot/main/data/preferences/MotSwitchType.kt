package dev.nelson.mot.main.data.preferences

import androidx.datastore.preferences.core.Preferences
import dev.nelson.mot.main.presentations.screen.settings.SettingsScreen

/**
 * Used to identify the type of switch on the [SettingsScreen] that is being set.
 */
sealed class MotSwitchType(val key: Preferences.Key<Boolean>) {

    object DarkTheme : MotSwitchType(PreferencesKeys.DARK_THEME_ENABLED)

    object DynamicColorTheme : MotSwitchType(PreferencesKeys.DYNAMIC_COLOR_THEME_ENABLED)

    object ShowCents : MotSwitchType(PreferencesKeys.SHOW_CENTS_ENABLED)
}
