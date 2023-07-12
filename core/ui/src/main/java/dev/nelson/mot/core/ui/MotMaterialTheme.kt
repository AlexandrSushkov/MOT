package dev.nelson.mot.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.nelson.mot.core.ui.fundation.motIsDarkTheme
import dev.nelson.mot.core.ui.view_state.AppThemeViewState
import dev.theme.DarkColorScheme
import dev.theme.LightColorScheme
import dev.theme.MotTypography

/**
 * Them for the whole app.
 *
 * USE MATERIAL3 widgets to match this theme!!!!
 */
@Composable
fun MotMaterialTheme(
    appThemeViewState: AppThemeViewState = AppThemeViewState(),
    content: @Composable () -> Unit
) {
    val isDarkTheme = motIsDarkTheme(appThemeViewState = appThemeViewState)

    val colorScheme = when {
        appThemeViewState.dynamicColorThemeEnabled -> {
            val context = LocalContext.current
            if (isDarkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val systemUiController = rememberSystemUiController()
    val appBarColor = colorScheme.surface
    DisposableEffect(systemUiController, appBarColor) {
        systemUiController.setSystemBarsColor(color = appBarColor)
        systemUiController.setNavigationBarColor(color = appBarColor)
        onDispose {}
    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.surface.toArgb()
//            window.navigationBarColor = colorScheme.surface.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MotTypography,
        content = content
    )
}
