@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.core.ui

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview

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

@Preview(showBackground = true)
@Composable
private fun MotTopAppBarPreview() {
    Column {
        MotMaterialTheme(darkTheme = false) {
            MotTopAppBarDarkPreviewData()
        }
        MotMaterialTheme(darkTheme = true) {
            MotTopAppBarDarkPreviewData()
        }
        MotMaterialTheme(dynamicColor = true) {
            MotTopAppBarDarkPreviewData()
        }
    }
}

@Composable
private fun MotTopAppBarDarkPreviewData() {
    MotTopAppBar(
        appBarTitle = "Toolbar",
        navigationIcon = { MotNavDrawerIcon {} },
        actions = { MotNavSettingsIcon {} }
    )
}

@Composable
fun MotSelectionTopAppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(Icons.Default.Close, contentDescription = "close drawer icon")
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = actions,
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    )
}

@Preview(showSystemUi = false, group = "toolbar")
@Composable
private fun MotSelectionTopAppBarPreview() {
    Column {
        MotMaterialTheme(darkTheme = false) {
            MotSelectionTopAppBarPreviewData()
        }
        MotMaterialTheme(darkTheme = true) {
            MotSelectionTopAppBarPreviewData()
        }
        MotMaterialTheme(dynamicColor = true) {
            MotSelectionTopAppBarPreviewData()
        }
    }
}

@Composable
private fun MotSelectionTopAppBarPreviewData() {
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
