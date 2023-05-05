package dev.nelson.mot.core.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.theme.DarkColorScheme
import dev.theme.LightColorScheme
import dev.theme.MotTypography

/**
 * USE MATERIAL3 widgets to match this theme!!!!
 */
@Composable
fun MotMaterialTheme(
    forceDark: Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme || forceDark -> DarkColorScheme
        else -> LightColorScheme
    }

//    val context = LocalContext.current
//    val colorScheme = if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !(darkTheme || forceDark)
        }
    }

//    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
//    val context = LocalContext.current
//    val colorScheme = if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//    val typography = if (darkTheme) MotTypographyDark else MotTypographyLight

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MotTypography,
        content = content
    )
}
