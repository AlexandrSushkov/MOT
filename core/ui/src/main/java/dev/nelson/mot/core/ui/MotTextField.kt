@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package dev.nelson.mot.core.ui

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MotTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        value = value,
        modifier = modifier,
        singleLine = singleLine,
        maxLines = maxLines,
        onValueChange = onValueChange,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}


@Preview(
    showBackground = true,
    group = "MotTextFieldLight",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun MotTextFieldPreviewLightOn() {
    PreviewDataTextField()
}

@Preview(showBackground = true, group = "MotTextFieldLight")
@Composable
fun MotTextFieldPreviewLightOff() {
    PreviewDataTextField()
}

@Preview(
    showBackground = true,
    group = "MotTextFieldDark",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun MotTextFieldPreviewDarkOn() {
    MotMaterialTheme(darkTheme = true) {
        PreviewDataTextField()
    }
}

@Preview(
    showBackground = true,
    group = "MotTextFieldDark",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun MotTextFieldPreviewDarkOff() {
    MotMaterialTheme(darkTheme = true) {
        PreviewDataTextField()
    }
}

@Preview(showBackground = true, group = "MotTextFieldDynamic")
@Composable
fun MotTextFieldPreviewDynamicOn() {
    MotMaterialTheme(dynamicColor = true) {
        PreviewDataTextField()
    }
}

@Preview(showBackground = true, group = "MotTextFieldDynamic")
@Composable
fun MotTextFieldPreviewDynamicOff() {
    MotMaterialTheme(dynamicColor = true) {
        PreviewDataTextField()
    }
}

@Composable
private fun PreviewDataTextField() {
    ListItem(
        headlineContent = {
            MotTextField(
                TextFieldValue("Hello World!"),
                onValueChange = {},
                placeholder = { Text("Placeholder") }
            )
        }
    )
}