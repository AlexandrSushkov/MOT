package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MotButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        content = content
    )
}

@Composable
fun MotOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        content = content
    )
}

@Preview(showBackground = true, group = "regularButton")
@Composable
fun MotButtonPreview() {
    MotButton(
        onClick = {},
        modifier = Modifier,
        content = {
            Icon(
                Icons.Default.Save,
                modifier = Modifier.padding(end = 4.dp),
                contentDescription = "IconButton"
            )
            Text(text = "Mot Button")
        }
    )
}

@Composable
fun MotNavBackIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.ArrowBack, contentDescription = "back icon")
    }
}

@Preview(showBackground = true, group = "imageButton")
@Composable
private fun MotNavIconsPreview() {
    Row {
        MotNavBackIcon {}
        MotNavDrawerIcon {}
        MotNavSettingsIconPreview()
    }
}

@Preview(showBackground = true, group = "imageButton")
@Composable
private fun MotNavBackIconPreview() {
    MotNavBackIcon {}
}

@Composable
fun MotNavDrawerIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.Menu, contentDescription = "drawer menu icon")
    }
}

@Preview(showBackground = true, group = "imageButton")
@Composable
private fun MotNavDrawerIconPreview() {
    MotNavDrawerIcon {}
}

@Composable
fun MotNavSettingsIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.Settings, contentDescription = "settings icon")
    }
}

@Preview(showBackground = true, group = "imageButton")
@Composable
private fun MotNavSettingsIconPreview() {
    MotNavSettingsIcon {}
}

@Composable
fun MotSaveIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            Icons.Default.Save,
            contentDescription = "save icon"
        )
    }
}

@Preview(showBackground = true, group = "imageButton")
@Composable
fun MotSaveIconButtonPreview() {
    MotSaveIcon {}
}
