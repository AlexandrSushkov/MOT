@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.core.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import dev.utils.preview.MotPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotTopAppBar(
    appBarTitle: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    ),
) {
    val systemUiController = rememberSystemUiController()

    /**
     * Depending on this value color of the status bar will be changed.
     * Toolbar color will be changed depending on [scrollBehavior]'s
     */
    val colorTransitionFraction = scrollBehavior.state.overlappedFraction
    val fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
    val appBarColor = if (fraction == 1f) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surface
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

@MotPreview
@Composable
private fun MotTopAppBarPreview() {
    MotMaterialTheme {
        MotTopAppBar(
            appBarTitle = "Toolbar",
            navigationIcon = { MotNavDrawerIcon {} },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotSelectionTopAppBar(
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

@MotPreview
@Composable
private fun MotSelectionTopAppBarPreview() {
    MotMaterialTheme {
        MotSelectionTopAppBar(
            title = "1",
            navigationIcon = { MotCloseIcon {} },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Category, contentDescription = "")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Delete, contentDescription = "")
                }
            }
        )
    }
}
