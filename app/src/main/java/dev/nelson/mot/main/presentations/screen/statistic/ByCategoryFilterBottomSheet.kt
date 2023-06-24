package dev.nelson.mot.main.presentations.screen.statistic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.domain.use_case.statistic.StatisticByCategoryPerMonthModel
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder

/**
 * @model list of categories to select from
 */
@Composable
fun ByCategoryFilterBottomSheet(
    model: List<StatisticByCategoryPerMonthModel>,
    onItemSelected: (StatisticByCategoryPerMonthModel) -> Unit,
    hideBottomSheetCallback: () -> Unit,
    selectedMonthModel: StatisticByCategoryPerMonthModel
) {
    val layColumnState = rememberLazyListState()

    Surface {
        if (model.isEmpty()) {
            ListPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp)
            )
        } else {
            LazyColumn(
                state = layColumnState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(model) {
                    ListItem(
                        modifier = Modifier.clickable {
                            onItemSelected.invoke(it)
                            hideBottomSheetCallback.invoke()
                        },
                        headlineContent = { Text(text = it.category?.name ?: "no category") },
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
    }
}