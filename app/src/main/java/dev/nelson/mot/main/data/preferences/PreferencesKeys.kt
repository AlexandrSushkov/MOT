package dev.nelson.mot.main.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val DARK_THEME_ENABLED = booleanPreferencesKey("dark_theme_enabled")
    val DYNAMIC_COLOR_THEME_ENABLED = booleanPreferencesKey("dynamic_color_theme_enabled")
    val SHOW_CENTS_ENABLED = booleanPreferencesKey("show_cents_enabled")
}