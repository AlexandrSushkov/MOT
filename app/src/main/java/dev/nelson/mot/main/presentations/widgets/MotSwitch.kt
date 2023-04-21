@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.widgets

import android.content.res.Configuration
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
import dev.nelson.mot.main.presentations.ui.theme.colorsMaterial3

@Composable
fun MotSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?
) {
//    Switch(
//        checked = checked,
//        onCheckedChange = onCheckedChange,
//        colors = SwitchDefaults.colorsMaterial3()
//    )
    androidx.compose.material3.Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
    )
}

@Preview(showBackground = true, group = "MotSwitchLight", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun MotSwitchPreviewLightOn() {
    ListItem(
        trailing = { MotSwitch(checked = true, onCheckedChange = {}) },
        text = {}
    )
}

@Preview(showBackground = true, group = "MotSwitchLight")
@Composable
fun MotSwitchPreviewLightOff() {
    ListItem(
        trailing = { MotSwitch(checked = false, onCheckedChange = {}) },
        text = {}
    )
}

@Preview(showBackground = true, group = "MotSwitchDark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun MotSwitchPreviewDarkOn() {
    MotTheme(darkTheme = true) {
        ListItem(
            trailing = { MotSwitch(checked = true, onCheckedChange = {}) },
            text = {}
        )
    }
}

@Preview(showBackground = true, group = "MotSwitchDark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun MotSwitchPreviewDarkOff() {
    MotTheme(darkTheme = true) {
        ListItem(
            trailing = { MotSwitch(checked = false, onCheckedChange = {}) },
            text = {}
        )
    }
}

@Preview(showBackground = true, group = "MotSwitchDynamic")
@Composable
fun MotSwitchPreviewDynamicOn() {
    MotTheme(dynamicColor = true) {
        ListItem(
            trailing = { MotSwitch(checked = true, onCheckedChange = {}) },
            text = {}
        )
    }
}

@Preview(showBackground = true, group = "MotSwitchDynamic")
@Composable
fun MotSwitchPreviewDynamicOff() {
    MotTheme(dynamicColor = true) {
        ListItem(
            trailing = { MotSwitch(checked = false, onCheckedChange = {}) },
            text = {}
        )
    }
}