package dev.nelson.mot.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.utils.preview.MotPreview

@Composable
fun MotTextButton(
    modifier: Modifier = Modifier,
    @StringRes stringResource: Int,
    onClick: () -> Unit
) {
    val text = stringResource(id = stringResource)
    MotTextButton(
        onClick = onClick,
        modifier = modifier,
        text = text
    )
}

@Composable
fun MotTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@MotPreview
@Composable
private fun MotTextButtonPreview() {
    MotMaterialTheme {
        ListItem(headlineContent = {
            MotTextButton(
                onClick = {},
                text = "Button"
            )
        })
    }
}

@Composable
fun MotButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        content = content
    )
}

@MotPreview
@Composable
private fun MotButtonPreview() {
    MotMaterialTheme {
        ListItem(
            headlineContent = {
                MotButton(
                    onClick = {},
                    modifier = Modifier,
                    content = {
                        MotIcons.Save(Modifier.padding(end = 4.dp))
                        Text(text = "Button")
                    }
                )
            }
        )
    }
}

@Composable
fun MotOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        content = content
    )
}

@MotPreview
@Composable
private fun MotOutlinePreview() {
    MotMaterialTheme {
        ListItem(
            headlineContent = {
                MotOutlinedButton(
                    onClick = {},
                    modifier = Modifier,
                    content = {
                        MotIcons.Save(Modifier.padding(end = 4.dp))
                        Text(text = "Button")
                    }
                )
            }
        )
    }
}
