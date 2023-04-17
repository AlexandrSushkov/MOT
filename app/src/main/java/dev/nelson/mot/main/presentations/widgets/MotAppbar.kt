@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.widgets

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
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.nelson.mot.main.presentations.ui.theme.MotTheme

@Composable
fun TopAppBarMot(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        navigationIcon = navigationIcon,
        title = { Text(text = title) },
        actions = actions
    )
}

@Preview(showBackground = true)
@Composable
private fun ToolbarMotLightPreview() {
    MotTheme(darkTheme = false) {
        TopAppBarMot(
            title = "Toolbar",
            navigationIcon = { MotNavDrawerIcon {} },
            actions = { MotNavSettingsIcon {} }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ToolbarMotDarkPreview() {
    MotTheme(darkTheme = true) {
        TopAppBarMot(
            title = "Toolbar",
            navigationIcon = { MotNavDrawerIcon {} },
            actions = { MotNavSettingsIcon {} }
        )
    }
}

@Composable
fun MotSelectionTopAppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    SmallTopAppBar(
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

@Preview(showBackground = true)
@Composable
private fun MotSelectionTopAppBarLightPreview() {
    MotTheme(darkTheme = false) {
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

@Preview(showBackground = true)
@Composable
private fun MotSelectionTopAppBarDarkPreview() {
    MotTheme(darkTheme = true) {
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
