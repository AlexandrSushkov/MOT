package dev.nelson.mot.core.ui.fundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import dev.nelson.mot.core.ui.model.MotAppTheme
import dev.nelson.mot.core.ui.view_state.AppThemeViewState

/**
 * Combination of compose darkTheme method and mot application selected theme.
 */
@Composable
@ReadOnlyComposable
fun motIsDarkTheme(appThemeViewState: AppThemeViewState): Boolean {
    return when (appThemeViewState.selectedTheme) {
        is MotAppTheme.Dark -> true
        is MotAppTheme.Light -> false
        is MotAppTheme.System -> isSystemInDarkTheme()
    }
}
