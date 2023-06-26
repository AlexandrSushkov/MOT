package dev.nelson.mot.main.presentations.screen.statistic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview

@Composable
fun ByTimeFilterBottomSheet(
    model: List<StatisticByMonthModel>,
    selectedMonthModel: StatisticByMonthModel,
    onItemSelected: (StatisticByMonthModel) -> Unit,
    hideBottomSheetCallback: () -> Unit
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
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(model) {
                    val containerColor = if (it == selectedMonthModel) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                    ListItem(
                        modifier = Modifier.clickable {
                            onItemSelected.invoke(it)
                            hideBottomSheetCallback.invoke()
                        },
                        headlineContent = { Text(text = it.monthText) },
                        trailingContent = {
                            if (it == selectedMonthModel) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = stringResource(id = R.string.accessibility_done_icon)
                                )
                            }
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = containerColor
                        )
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
    MotMaterialTheme {
        ByTimeFilterBottomSheet(
            model = modelList,
            selectedMonthModel = modelList.first(),
            onItemSelected = {},
            hideBottomSheetCallback = {}
        )
    }
}
