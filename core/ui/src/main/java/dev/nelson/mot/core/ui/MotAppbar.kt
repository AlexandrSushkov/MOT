@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.core.ui

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import dev.utils.preview.MotPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotTopAppBar(
    appBarTitle: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    screenContentScrollingState: LazyListState = rememberLazyListState()
) {

    /**
     * Depending on this value color of the status bar and toolbar will be changed.
     */
    val isScreenContentScrolling = remember {
        derivedStateOf { screenContentScrollingState.firstVisibleItemIndex != 0 }
    }

    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
        if (isScreenContentScrolling.value) {
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
