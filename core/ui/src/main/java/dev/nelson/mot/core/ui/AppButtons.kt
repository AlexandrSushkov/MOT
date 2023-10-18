package dev.nelson.mot.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.utils.preview.MotPreview

object AppButtons {

    @Composable
    fun TextButton(
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        @StringRes stringResource: Int,
        onClick: () -> Unit
    ) {
        val text = stringResource(id = stringResource)
        TextButton(
            modifier = modifier,
            enabled = enabled,
            text = text,
            onClick = onClick
        )
    }

    @Composable
    fun TextButton(
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        text: String,
        onClick: () -> Unit
    ) {
        TextButton(
            modifier = modifier,
            enabled = enabled,
            onClick = onClick
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

    @Composable
    fun Regular(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        content: @Composable RowScope.() -> Unit
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            content = content
        )
    }

    @Composable
    fun Outlined(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        content: @Composable RowScope.() -> Unit
    ) {
        OutlinedButton(
            modifier = modifier,
            onClick = onClick,
            content = content
        )
    }
}

@MotPreview
@Composable
private fun TextButtonPreview() {
    AppTheme {
        Surface {
            Column {
                AppButtons.TextButton(text = "Text") {}
                AppButtons.Regular(
                    modifier = Modifier,
                    onClick = {},
                ) {
                    AppIcons.Save(Modifier.padding(end = 4.dp))
                    Text(text = "Regular")
                }
                AppButtons.Outlined(
                    modifier = Modifier,
                    onClick = {},
                ) {
                    AppIcons.Save(Modifier.padding(end = 4.dp))
                    Text(text = "Outlined")
                }
            }
        }
    }
}
