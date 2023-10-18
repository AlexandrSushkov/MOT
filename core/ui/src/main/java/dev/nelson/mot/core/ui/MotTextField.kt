@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.core.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import dev.utils.preview.MotPreview

@Composable
fun MotTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    minLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = Int.MAX_VALUE,
    shape: Shape = TextFieldDefaults.shape,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    textStyle: TextStyle = LocalTextStyle.current,
    ) {
    TextField(
        value = value,
        modifier = modifier,
        singleLine = singleLine,
        minLines = minLines,
        leadingIcon = leadingIcon,
        maxLines = maxLines,
        onValueChange = onValueChange,
        placeholder = placeholder,
        shape = shape,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors,
        textStyle = textStyle
    )
}


@MotPreview
@Composable
fun MotTextFieldPreview() {
    AppTheme {
        ListItem(
            headlineContent = {
                MotTextField(
                    value = TextFieldValue("Hello World!"),
                    onValueChange = {},
                    placeholder = { Text("Placeholder") }
                )
            }
        )
    }
}
