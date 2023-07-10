package dev.nelson.mot.core.ui.fundation

import android.view.RoundedCorner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Calculate device display corner radius based on windows insets.
 */
@Composable
@ReadOnlyComposable
fun getDisplayCornerRadius(defaultDp: Dp = 0.dp): Dp {
    return LocalView.current
        .rootView
        .rootWindowInsets
        .getRoundedCorner(RoundedCorner.POSITION_TOP_RIGHT)
        ?.radius?.let { radius ->
            with(LocalDensity.current) {
                radius.toDp()
            }
        }
        ?: defaultDp
}
