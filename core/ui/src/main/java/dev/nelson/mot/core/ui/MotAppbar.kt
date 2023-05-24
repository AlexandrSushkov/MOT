@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.core.ui

import android.app.Activity
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.utils.preview.MotPreview

/**
 * @param isScrolling it means the content on the screen is scrolling.
 * Depending on this value color of the status bar and toolbar will be changed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotTopAppBar(
    appBarTitle: String,
    isScrolling: Boolean = false,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    if (isScrolling) {
        val appBarColor = MaterialTheme.colorScheme.secondaryContainer
        val toolbarColors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
        CenterAlignedTopAppBar(
            navigationIcon = navigationIcon,
            title = {
                Text(
                    text = appBarTitle,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = toolbarColors,
            actions = actions
        ).also {
            val view = LocalView.current
            if (!view.isInEditMode) {
                val window = (view.context as Activity).window
                window.statusBarColor = appBarColor.toArgb()
            }
        }
    } else {
        val appBarColor = MaterialTheme.colorScheme.surface
        val toolbarColors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
        CenterAlignedTopAppBar(
            navigationIcon = navigationIcon,
            title = {
                Text(
                    text = appBarTitle,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = toolbarColors,
            actions = actions
        ).also {
            val view = LocalView.current
            if (!view.isInEditMode) {
                val window = (view.context as Activity).window
                window.statusBarColor = appBarColor.toArgb()
            }
        }
    }
}

@MotPreview
@Composable
private fun MotTopAppBarPreview() {
    MotMaterialTheme {
        MotTopAppBar(
            appBarTitle = "Toolbar",
//            isScrolling = true,
            navigationIcon = { MotNavDrawerIcon {} },
            actions = { MotNavSettingsIcon {} }
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
                style = MaterialTheme.typography.headlineSmall
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
