package dev.nelson.mot.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import dev.utils.preview.MotPreview

@Composable
fun MotSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    uncheckedStateIcon: ImageVector? = null,
    checkedStateIcon: ImageVector = Icons.Default.Done
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        thumbContent = {
            if (checked) {
                Icon(
                    checkedStateIcon,
                    modifier = Modifier.scale(0.75f),
                    contentDescription = "switch checked icon"
                )
            } else {
                uncheckedStateIcon?.let {
                    Icon(
                        it,
                        modifier = Modifier.scale(0.75f),
                        contentDescription = "switch unchecked icon"
                    )
                }
            }
        }
    )
}

@MotPreview
@Composable
fun MotSwitchPreview() {
    AppTheme {
        Column {
            MotSwitchPreviewData(true)
            MotSwitchPreviewData(false)
        }
    }
}

@Composable
private fun MotSwitchPreviewData(checked: Boolean) {
    ListItem({ MotSwitch(checked = checked, onCheckedChange = {}) })
}
