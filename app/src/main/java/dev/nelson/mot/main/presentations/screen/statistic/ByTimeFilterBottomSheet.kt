package dev.nelson.mot.main.presentations.screen.statistic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.widget.AppIcons
import dev.nelson.mot.main.presentations.widgets.EmptyListPlaceholder
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview

@Composable
fun ByTimeFilterBottomSheet(
    model: List<StatisticByMonthModel>,
    selectedMonthModel: StatisticByMonthModel,
    onItemSelected: (StatisticByMonthModel) -> Unit
) {
    val layColumnState = rememberLazyListState()

    Surface {
        if (model.isEmpty()) {
            EmptyListPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp)
            )
        } else {
            LazyColumn(
                state = layColumnState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(model) {
                    val containerColor = with(MaterialTheme.colorScheme) {
                        if (it == selectedMonthModel) secondaryContainer else surface
                    }
                    ListItem(
                        modifier = Modifier.clickable { onItemSelected.invoke(it) },
                        headlineContent = { Text(text = it.monthText) },
                        trailingContent = { if (it == selectedMonthModel) AppIcons.Done() },
                        colors = ListItemDefaults.colors(containerColor)
                    )
                }
            }
        }
    }
}

@MotPreview
@Composable
private fun ByTimeFilterBottomSheetPreview() {
    val modelList = PreviewData.statisticByMonthListPreviewData
    AppTheme {
        ByTimeFilterBottomSheet(
            model = modelList,
            selectedMonthModel = modelList.first()
        ) {}
    }
}
