package dev.nelson.mot.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import dev.utils.preview.MotPreview

@Composable
fun MotSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
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
