package dev.nelson.mot.main.presentations.screen.statistic.new_tabs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.presentations.screen.statistic.SelectedTimeViewState
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByMonthModel
import dev.nelson.mot.main.presentations.widgets.FABFooter
import dev.nelson.mot.main.presentations.widgets.MotModalBottomSheetLayout
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun StatisticByTimeTabLayout(
    scrollBehavior: TopAppBarScrollBehavior,
    selectedTimeViewState: SelectedTimeViewState,
    model: List<StatisticByMonthModel>,
    onMonthModelSelected: (StatisticByMonthModel) -> Unit,
) {
    val switchState = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val layColumnState = rememberLazyListState()
    val selectedMonthModel = selectedTimeViewState.selectedTimeModel

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        content = {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f, true)
                        .padding(16.dp)
                ) {
                    PieChart(
                        pieChartData = selectedTimeViewState.selectedTimePieChartData,
                        modifier = Modifier.fillMaxWidth(),
                        sliceDrawer = SimpleSliceDrawer()

                    )
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = "${selectedMonthModel.month}/${selectedMonthModel.year}",
                                    style = MaterialTheme.typography.titleLarge,

                                    )

                            },
                            trailingContent = {
                                Text(
                                    text = "${selectedMonthModel.sumOfCategories}",
                                    style = MaterialTheme.typography.titleLarge,

                                    )

                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                        )
                        Divider()
                        Column {
                            selectedMonthModel.categoriesModelList.forEach {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = it.category?.name ?: "No category",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    Text(
                                        text = it.sumOfPayments.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item {
                FABFooter()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
private fun StatisticByTimeTabLayoutPreview() {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    MotMaterialTheme {
        StatisticByTimeTabLayout(
            scrollBehavior = behavior,
            model = PreviewData.statisticByMonthListPreviewData,
            selectedTimeViewState = SelectedTimeViewState(
                selectedTimeModel = PreviewData.statisticByMonthModelPreviewData,
            ),
            onMonthModelSelected = {}
        )
    }
}
