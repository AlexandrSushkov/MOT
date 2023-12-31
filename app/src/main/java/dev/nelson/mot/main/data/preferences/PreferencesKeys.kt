package dev.nelson.mot.main.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val APP_THEME = stringPreferencesKey("app_theme")
    val DYNAMIC_COLOR_THEME_ENABLED = booleanPreferencesKey("dynamic_color_theme_enabled")
    val SHOW_CENTS_ENABLED = booleanPreferencesKey("show_cents_enabled")
    val SHOW_CURRENCY_SYMBOL_ENABLED = booleanPreferencesKey("show_currency_symbol_enabled")
    val SHOW_DIGITS_ENABLED = booleanPreferencesKey("show_digits_enabled")
    val SELECTED_COUNTRY_CODE = stringPreferencesKey("selected_country_code")
}
