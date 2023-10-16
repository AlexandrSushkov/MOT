package dev.nelson.mot.core.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.utils.preview.MotPreview

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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Close(
        modifier: Modifier = Modifier,
        enableTooltip: Boolean = false,
        onClick: () -> Unit
    ) {
        if (enableTooltip) {
            PlainTooltipBox(tooltip = { Text(text = "Close") }) {
                IconButton(
                    modifier = modifier.tooltipAnchor(),
                    onClick = onClick
                ) {
                    MotIcons.Close()
                }
            }
        } else {
            IconButton(
                modifier = modifier,
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
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            MotIcons.Category()
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
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            MotIcons.EditCalendar()
        }
    }

    @Composable
    fun Delete(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            MotIcons.Delete()
        }
    }
}

@MotPreview
@Composable
private fun MotIconsPreview() {
    MotMaterialTheme {
        LazyColumn(
            content = {
                item { Surface { MotIconButtons.Settings {} } }
                item { Surface { MotIconButtons.Save {} } }
                item { Surface { MotIconButtons.Filter {} } }
                item { Surface { MotIconButtons.Drawer {} } }
                item { Surface { MotIconButtons.Back {} } }
                item { Surface { MotIconButtons.Close {} } }
            }
        )
    }
}
