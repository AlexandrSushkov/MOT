package dev.nelson.mot.core.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.utils.preview.MotPreview

object MotIcons {

    @Composable
    fun Filter(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current,
    ) {
        Icon(
            Icons.Default.FilterList,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_filter_icon)
        )
    }

    @Composable
    fun Save(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.Save,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_save_icon)
        )
    }

    @Composable
    fun Settings(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.Settings,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_settings_icon)
        )
    }

    @Composable
    fun Drawer(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.Menu,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_drawer_icon)
        )
    }

    @Composable
    fun Category(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.Category,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_category_icon)
        )
    }

    @Composable
    fun Info(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_info_icon)
        )
    }

    @Composable
    fun Add(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.Add,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_add_icon)
        )
    }

    @Composable
    fun FavoriteChecked(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Filled.Star,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_favorite_checked_icon)
        )
    }

    @Composable
    fun FavoriteUnchecked(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Filled.StarBorder,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_favorite_unchecked_icon)
        )
    }

    @Composable
    fun Done(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_done_icon)
        )
    }

    @Composable
    fun Delete(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.Delete,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_delete_icon)
        )
    }

    @Composable
    fun Calendar(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.CalendarMonth,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_calendar_icon)
        )
    }

    @Composable
    fun EditCalendar(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.EditCalendar,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_calendar_icon)
        )
    }

    @Composable
    fun Search(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.Search,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_search_icon)
        )
    }

    @Composable
    fun Back(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.ArrowBack,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_back_icon)
        )
    }

    @Composable
    fun Close(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            Icons.Default.Close,
            modifier = modifier,
            tint = tint,
            contentDescription = stringResource(R.string.content_description_close_icon)
        )
    }
}

@MotPreview
@Composable
private fun MotIconsPreview() {
    val modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .aspectRatio(1f)

    MotMaterialTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            content = {
                item { Surface { MotIcons.Filter(modifier) } }
                item { Surface { MotIcons.Save(modifier) } }
                item { Surface { MotIcons.Settings(modifier) } }
                item { Surface { MotIcons.Drawer(modifier) } }
                item { Surface { MotIcons.Category(modifier) } }
                item { Surface { MotIcons.Info(modifier) } }
                item { Surface { MotIcons.Add(modifier) } }
                item { Surface { MotIcons.FavoriteChecked(modifier) } }
                item { Surface { MotIcons.FavoriteUnchecked(modifier) } }
                item { Surface { MotIcons.Done(modifier) } }
                item { Surface { MotIcons.Delete(modifier) } }
                item { Surface { MotIcons.Calendar(modifier) } }
                item { Surface { MotIcons.EditCalendar(modifier) } }
                item { Surface { MotIcons.Search(modifier) } }
                item { Surface { MotIcons.Back(modifier) } }
                item { Surface { MotIcons.Close(modifier) } }
            }
        )
    }
}
