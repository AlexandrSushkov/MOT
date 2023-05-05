@file:OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package dev.nelson.mot.core.ui

import android.content.res.Configuration
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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

@Preview(
    showBackground = true,
    group = "MotSwitchLight",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun MotSwitchPreviewLightOn() {
    MotSwitchPreviewData(true)
}

@Preview(showBackground = true, group = "MotSwitchLight")
@Composable
fun MotSwitchPreviewLightOff() {
    MotSwitchPreviewData(false)
}

@Preview(
    showBackground = true,
    group = "MotSwitchDark",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun MotSwitchPreviewDarkOn() {
    MotMaterialTheme(darkTheme = true) {
        MotSwitchPreviewData(true)
    }
}

@Preview(
    showBackground = true,
    group = "MotSwitchDark",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun MotSwitchPreviewDarkOff() {
    MotMaterialTheme(darkTheme = true) {
        MotSwitchPreviewData(false)
    }
}

@Preview(showBackground = true, group = "MotSwitchDynamic")
@Composable
fun MotSwitchPreviewDynamicOn() {
    MotMaterialTheme(dynamicColor = true) {
        MotSwitchPreviewData(true)
    }
}

@Preview(showBackground = true, group = "MotSwitchDynamic")
@Composable
fun MotSwitchPreviewDynamicOff() {
    MotMaterialTheme(dynamicColor = true) {
        MotSwitchPreviewData(false)
    }
}

@Composable
private fun MotSwitchPreviewData(checked: Boolean) {
    ListItem(
        trailing = { MotSwitch(checked = checked, onCheckedChange = {}) },
        text = {}
    )
}