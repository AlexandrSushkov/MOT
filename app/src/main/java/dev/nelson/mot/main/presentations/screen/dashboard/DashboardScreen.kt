package dev.nelson.mot.main.presentations.screen.dashboard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.MotDismissibleListItem
import dev.nelson.mot.core.ui.widget.AppIconButtons
import dev.nelson.mot.core.ui.widget.AppToolbar
import dev.nelson.mot.main.data.model.MotListItemModel
import dev.utils.preview.MotPreview

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {}
) {
    val items by viewModel.itemsState.collectAsState(initial = emptyList())
    DashboardScreenLayout(
        appBarTitle = appBarTitle,
        appBarNavigationIcon = appBarNavigationIcon,
        onItemSwiped = { viewModel.onItemSwiped(it) },
        onUndoClicked = { viewModel.onUndoClicked() },
        items = items
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenLayout(
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit,
    onItemSwiped: (MotListItemModel.Item) -> Unit = {},
    onUndoClicked: () -> Unit = {},
    items: List<MotListItemModel>
) {
    Scaffold(
        topBar = {
            AppToolbar.Regular(
                appBarTitle = appBarTitle,
                navigationIcon = appBarNavigationIcon
            )
        }
    ) { innerPadding ->
        val directions = setOf(DismissDirection.EndToStart)
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                Row {
                    FloatingActionButton(onClick = { onUndoClicked() }) {
                        Text(text = "reveal")
                    }
                    FloatingActionButton(onClick = {
                        (items.first() as? MotListItemModel.Item)?.let {
                            onItemSwiped(it)
                        }
                    }) {
                        Text(text = "hide")
                    }
                }
            }
            items.forEach {
                item(key = it.key) {
                    when (it) {
                        is MotListItemModel.Item -> {
                            MotDismissibleListItem(
                                isShow = it.isShow,
                                onItemSwiped = { onItemSwiped(it) },
                                directions = directions,
                                dismissContent = {
                                    ListItem(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        headlineContent = {
                                            Text(text = it.category.name)
                                        }
                                    )
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

@MotPreview
@Composable
private fun DashboardScreenLayoutPreview() {
    AppTheme {
        DashboardScreenLayout(
            appBarTitle = "Dashboard",
            appBarNavigationIcon = {
                AppIconButtons.Drawer { }
            },
            items = emptyList()
        )
    }
}
