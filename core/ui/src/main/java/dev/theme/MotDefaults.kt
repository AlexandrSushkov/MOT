package dev.theme

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TextFieldDefaults.textFieldMaterial3Colors(): TextFieldColors {
    return this.textFieldColors(
        textColor = LocalContentColor.current.copy(LocalContentAlpha.current),
        backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = BackgroundOpacity),
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high),
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = UnfocusedIndicatorLineOpacity),
        errorIndicatorColor = MaterialTheme.colorScheme.error,
        leadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = IconOpacity),
        trailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = IconOpacity),
        errorTrailingIconColor = MaterialTheme.colorScheme.error,
        focusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high),
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
        errorLabelColor = MaterialTheme.colorScheme.error,
        placeholderColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
    )
}

@Composable
fun TextFieldDefaults.outlinedTextFieldMaterial3Colors(): TextFieldColors {
    return this.outlinedTextFieldColors(
        textColor = LocalContentColor.current.copy(LocalContentAlpha.current),
        backgroundColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high),
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),
        errorBorderColor = MaterialTheme.colorScheme.error,
        leadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = IconOpacity),
        trailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = IconOpacity),
        errorTrailingIconColor = MaterialTheme.colorScheme.error,
        focusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high),
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
        errorLabelColor = MaterialTheme.colorScheme.error,
        placeholderColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
    )
}

@Composable
fun SwitchDefaults.colorsMaterial3(): SwitchColors {
    return this.colors(
        checkedThumbColor = MaterialTheme.colorScheme.secondaryContainer,
        uncheckedThumbColor = MaterialTheme.colorScheme.surface,
        uncheckedTrackColor = MaterialTheme.colorScheme.onSurface,
    )
}
