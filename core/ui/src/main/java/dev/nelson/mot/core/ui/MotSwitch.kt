package dev.nelson.mot.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import dev.utils.preview.MotPreview

@Composable
fun MotSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        thumbContent = {
            if (checked) {
                Icon(
                    Icons.Default.Done,
                    modifier = Modifier.scale(0.75f),
                    contentDescription = "back icon"
                )
            } else {
                Icon(
                    Icons.Default.Close,
                    modifier = Modifier.scale(0.75f),
                    contentDescription = "back icon"
                )
            }
        }
    )
}

@MotPreview
@Composable
fun MotSwitchPreview() {
    MotMaterialTheme {
        Column {
            MotSwitchPreviewData(true)
            MotSwitchPreviewData(false)
        }
    }
}

@Composable
private fun MotSwitchPreviewData(checked: Boolean) {
    ListItem(
        headlineContent = { MotSwitch(checked = checked, onCheckedChange = {}) },
    )
}
