package dev.nelson.mot.core.ui

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
import dev.utils.preview.MotPreview

@OptIn(ExperimentalMaterial3Api::class)
object MotIconButtons {
    @Composable
    fun Settings(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            MotIcons.Settings()
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
            MotIcons.Save()
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
            MotIcons.Filter()
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
            MotIcons.Drawer()
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
            MotIcons.Back()
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
                MotIcons.Close()
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
                MotIcons.Category()
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
            MotIcons.Calendar()
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
                MotIcons.EditCalendar()
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
                MotIcons.Delete()
            }
        }
    }
}

@MotPreview
@Composable
private fun MotIconsPreview() {
    MotMaterialTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            content = {
                item { Surface { MotIconButtons.Settings {} } }
                item { Surface { MotIconButtons.Save {} } }
                item { Surface { MotIconButtons.Filter {} } }
                item { Surface { MotIconButtons.Drawer {} } }
                item { Surface { MotIconButtons.Back {} } }
                item { Surface { MotIconButtons.Close {} } }
                item { Surface { MotIconButtons.Category {} } }
                item { Surface { MotIconButtons.Calendar {} } }
                item { Surface { MotIconButtons.EditCalendar {} } }
                item { Surface { MotIconButtons.Delete {} } }
            }
        )
    }
}
