package dev.nelson.mot.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes stringResource: Int
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String
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
            MotTextButton(onClick = { /*TODO*/ }, text = "Button")
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
                        Icon(
                            Icons.Default.Save,
                            modifier = Modifier.padding(end = 4.dp),
                            contentDescription = "IconButton"
                        )
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
                        Icon(
                            Icons.Default.Save,
                            modifier = Modifier.padding(end = 4.dp),
                            contentDescription = "IconButton"
                        )
                        Text(text = "Button")
                    }
                )
            }
        )
    }
}

@Composable
fun MotNavBackIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.ArrowBack, contentDescription = "back icon")
    }
}

@Composable
fun MotCloseIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.Close, contentDescription = "close icon")
    }
}

@Composable
fun MotNavDrawerIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.Menu, contentDescription = "drawer menu icon")
    }
}

@Composable
fun MotNavSettingsIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.Settings, contentDescription = "settings icon")
    }
}

@Composable
fun MotSaveIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            Icons.Default.Save,
            contentDescription = "save icon"
        )
    }
}

@Composable
fun MotFilterIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            Icons.Default.FilterList,
            contentDescription = "save icon"
        )
    }
}

@MotPreview
@Composable
private fun MotIconsPreview() {
    MotMaterialTheme {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            content = {
                item { ListItem(headlineContent = { MotNavBackIcon {} }) }
                item { ListItem(headlineContent = { MotNavDrawerIcon {} }) }
                item { ListItem(headlineContent = { MotCloseIcon {} }) }
                item { ListItem(headlineContent = { MotNavSettingsIcon {} }) }
                item { ListItem(headlineContent = { MotSaveIcon {} }) }
                item { ListItem(headlineContent = { MotFilterIconButton {} }) }
            }
        )
    }
}
