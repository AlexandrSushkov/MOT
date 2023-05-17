@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.core.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import dev.utils.preview.MotPreview

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


@MotPreview
@Composable
fun MotTextFieldPreview() {
    MotMaterialTheme {
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
}
