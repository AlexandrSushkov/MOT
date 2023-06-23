package dev.nelson.mot.main.presentations.screen.statistic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ByTimeFilterBottomSheet(
    model: List<StatisticByMonthModel>,
    onItemSelected: (StatisticByMonthModel) -> Unit,
    hideBottomSheetCallback: () -> Unit,
    selectedMonthModel: StatisticByMonthModel
) {
    val layColumnState = rememberLazyListState()

    LazyColumn(
        state = layColumnState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(model) {
            ListItem(
                modifier = Modifier.clickable {
//                    onMonthModelSelected(it)
//                    coroutineScope.launch { modalBottomSheetState.hide() }
                    onItemSelected.invoke(it)
                    hideBottomSheetCallback.invoke()
                },
                headlineContent = { Text(text = "${it.month}/ ${it.year}") },
                trailingContent = {
                    if (it == selectedMonthModel) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    }
}
