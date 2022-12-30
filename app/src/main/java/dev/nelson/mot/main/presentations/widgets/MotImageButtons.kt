package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MotNavBackIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.ArrowBack, contentDescription = "back icon")
    }
}

@Preview(showBackground = true)
@Composable
private fun MotNavIconsPreview() {
    Row {
        MotNavBackIcon {}
        MotNavDrawerIcon {}
        MotNavSettingsIconPreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun MotNavBackIconPreview() {
    MotNavBackIcon {

    }
}

@Composable
fun MotNavDrawerIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.Menu, contentDescription = "drawer menu icon")
    }
}

@Preview(showBackground = true)
@Composable
private fun MotNavDrawerIconPreview() {
    MotNavDrawerIcon {

    }
}

@Composable
fun MotNavSettingsIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.Settings, contentDescription = "settings icon")
    }
}

@Preview(showBackground = true)
@Composable
private fun MotNavSettingsIconPreview() {
    MotNavSettingsIcon {

    }
}