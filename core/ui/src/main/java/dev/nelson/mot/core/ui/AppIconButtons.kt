package dev.nelson.mot.core.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.utils.preview.MotPreview

@OptIn(ExperimentalMaterial3Api::class)
object AppIconButtons {
    @Composable
    fun Settings(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            AppIcons.Settings()
        }
    }

    @Composable
    fun Save(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            AppIcons.Save()
        }
    }

    @Composable
    fun Filter(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            AppIcons.Filter()
        }
    }

    @Composable
    fun Drawer(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            AppIcons.Drawer()
        }
    }

    @Composable
    fun Back(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            AppIcons.Back()
        }
    }

    @Composable
    fun Close(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        PlainTooltipBox(tooltip = { Text(text = stringResource(id = R.string.tooltip_close_text)) }) {
            IconButton(
                modifier = modifier.tooltipAnchor(),
                onClick = onClick
            ) {
                AppIcons.Close()
            }
        }
    }

    @Composable
    fun Category(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        PlainTooltipBox(tooltip = { Text(text = stringResource(id = R.string.tooltip_category_text)) }) {
            IconButton(
                modifier = modifier.tooltipAnchor(),
                onClick = onClick
            ) {
                AppIcons.Category()
            }
        }
    }

    @Composable
    fun Calendar(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            AppIcons.Calendar()
        }
    }

    @Composable
    fun EditCalendar(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        PlainTooltipBox(tooltip = { Text(text = stringResource(id = R.string.tooltip_edit_calendar_text)) }) {
            IconButton(
                modifier = modifier.tooltipAnchor(),
                onClick = onClick
            ) {
                AppIcons.EditCalendar()
            }
        }
    }

    @Composable
    fun Delete(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        PlainTooltipBox(tooltip = { Text(text = stringResource(id = R.string.tooltip_delete_text)) }) {
            IconButton(
                modifier = modifier.tooltipAnchor(),
                onClick = onClick
            ) {
                AppIcons.Delete()
            }
        }
    }
}

@MotPreview
@Composable
private fun MotIconsPreview() {
    val modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .aspectRatio(1f)
    AppTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            content = {
                item { Surface { AppIconButtons.Settings(modifier) {} } }
                item { Surface { AppIconButtons.Save(modifier) {} } }
                item { Surface { AppIconButtons.Filter(modifier) {} } }
                item { Surface { AppIconButtons.Drawer(modifier) {} } }
                item { Surface { AppIconButtons.Back(modifier) {} } }
                item { Surface { AppIconButtons.Close(modifier) {} } }
                item { Surface { AppIconButtons.Category(modifier) {} } }
                item { Surface { AppIconButtons.Calendar(modifier) {} } }
                item { Surface { AppIconButtons.EditCalendar(modifier) {} } }
                item { Surface { AppIconButtons.Delete(modifier) {} } }
            }
        )
    }
}
