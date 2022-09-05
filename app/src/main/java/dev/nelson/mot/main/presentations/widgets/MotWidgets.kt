package dev.nelson.mot.main.presentations.widgets

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMot(
    title: String,
    onNavigationIconClick: () -> Unit,
    onActionIconClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(Icons.Default.Menu, contentDescription = "drawer icon")
            }
        },
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = onActionIconClick) {
                Icon(Icons.Default.Settings, contentDescription = "")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ToolbarMotPreview() {
    TopAppBarMot(
        title = "Toolbar",
        onNavigationIconClick = {},
        onActionIconClick = {}
    )
}
