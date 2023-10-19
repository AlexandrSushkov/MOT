package dev.nelson.mot.core.ui.widget

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.nelson.mot.core.ui.AppTheme
import dev.utils.preview.MotPreview

object AppToolbar {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Regular(
        appBarTitle: String,
        navigationIcon: @Composable () -> Unit = {},
        actions: @Composable RowScope.() -> Unit = {},
        scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
            rememberTopAppBarState()
        )
    ) {
        val systemUiController = rememberSystemUiController()

        /**
         * Depending on this value color of the status bar will be changed.
         * Toolbar color will be changed depending on [scrollBehavior]'s
         */
        val colorTransitionFraction = scrollBehavior.state.overlappedFraction
        val fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
        val appBarColor = with(MaterialTheme.colorScheme) {
            if (fraction == 1f) secondaryContainer else surface
        }
        val systemBarColor by animateColorAsState(
            targetValue = appBarColor,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "system_bar_animate_color"
        )

        DisposableEffect(systemUiController, systemBarColor) {
            systemUiController.setStatusBarColor(color = systemBarColor)
            onDispose {}
        }

        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            CenterAlignedTopAppBar(
                navigationIcon = navigationIcon,
                title = {
                    // TODO: use MotSingleLineText
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = appBarTitle,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                actions = actions,
                scrollBehavior = scrollBehavior
            )
        } else {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = appBarTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = actions
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Selectable(
        title: String,
        navigationIcon: @Composable () -> Unit = {},
        actions: @Composable RowScope.() -> Unit = {},
    ) {
        TopAppBar(
            navigationIcon = navigationIcon,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
private fun MotTopAppBarPreview() {
    AppTheme {
        AppToolbar.Regular(
            appBarTitle = "Toolbar",
            navigationIcon = { AppIconButtons.Drawer {} },
        )
    }
}

@MotPreview
@Composable
private fun MotSelectionTopAppBarPreview() {
    AppTheme {
        AppToolbar.Selectable(
            title = "1",
            navigationIcon = { AppIconButtons.Close {} },
            actions = {
                AppIconButtons.EditCalendar {}
                AppIconButtons.Category {}
                AppIconButtons.Delete {}
            }
        )
    }
}
