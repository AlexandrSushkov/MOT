@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.core.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
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
import dev.utils.preview.MotPreview

@Composable
fun MotTopAppBar(
    appBarTitle: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        navigationIcon = navigationIcon,
        title = {
            Text(
                text = appBarTitle,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = actions
    )
}

@MotPreview
@Composable
private fun MotTopAppBarPreview() {
    MotMaterialTheme {
        MotTopAppBar(
            appBarTitle = "Toolbar",
            navigationIcon = { MotNavDrawerIcon {} },
            actions = { MotNavSettingsIcon {} }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotSelectionTopAppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        navigationIcon = { MotCloseIcon { onNavigationIconClick } },
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
            onNavigationIconClick = {},
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
