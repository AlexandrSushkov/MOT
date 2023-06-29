package dev.nelson.mot.main.presentations.screen.statistic.new_tabs

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.PriceText
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.presentations.screen.statistic.SelectedTimeViewState
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByCategoryModel
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByMonthModel
import dev.nelson.mot.main.presentations.widgets.FABFooter
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder
import dev.nelson.mot.main.presentations.widgets.MotSingleLineText
import dev.nelson.mot.main.util.compose.PreviewData
import dev.theme.lightChartColors
import dev.utils.preview.MotPreview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun StatisticByTimeTabLayout(
    scrollBehavior: TopAppBarScrollBehavior,
    selectedTimeViewState: SelectedTimeViewState,
    model: List<StatisticByMonthModel>,
    onMonthModelSelected: (StatisticByMonthModel) -> Unit,
    onMonthCategoryClick: (StatisticByCategoryModel) -> Unit,
    priceViewState: PriceViewState
) {
    val switchState = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val layColumnState = rememberLazyListState()
    val selectedMonthModel = selectedTimeViewState.selectedTimeModel

    Surface(modifier = Modifier.fillMaxSize()) {
        if (selectedTimeViewState.selectedTimeModel.categoriesModelList.isEmpty()) {
            ListPlaceholder(modifier = Modifier.fillMaxSize())
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f, true),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp,
                    ),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        MotPieChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            selectedTimeViewState = selectedTimeViewState,
                            onPieEntrySelected = {},
                            onNothingSelected = {}
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = selectedMonthModel.monthText,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            PriceText(
                                price = selectedMonthModel.sumOfCategories,
                                priceViewState = priceViewState,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                Column {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                            .padding(horizontal = 16.dp),
                        content = {
                            item {
                                Column {
                                    selectedMonthModel.categoriesModelList.forEachIndexed { index, item ->
                                        Row(
                                            modifier = Modifier
                                                .padding(vertical = 8.dp)
                                                .fillMaxWidth()
                                                .clickable {
                                                    onMonthCategoryClick(item)
                                                },
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .background(selectedTimeViewState.selectedTimePieChartData.slices[index].color)
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                            MotSingleLineText(
                                                modifier = Modifier.weight(1f),
                                                text = item.category?.name ?: "No category",
                                                style = MaterialTheme.typography.titleMedium,
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            PriceText(
                                                price = item.sumOfPayments,
                                                priceViewState = priceViewState,
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .height(16.dp)
                                                    .width(2.dp)
                                                    .background(MaterialTheme.colorScheme.outlineVariant)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "%.1f%%".format(item.percentage),
                                                style = MaterialTheme.typography.titleMedium,
                                            )
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
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
private fun StatisticByTimeTabLayoutPreview() {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val modelList = PreviewData.statisticByMonthModelPreviewData
    val slices = modelList.categoriesModelList.map {
        PieChartData.Slice(
            value = it.sumOfPayments.toFloat(),
            color = lightChartColors.random()
        )
    }
    MotMaterialTheme {
        StatisticByTimeTabLayout(
            scrollBehavior = behavior,
            model = PreviewData.statisticByMonthListPreviewData,
            selectedTimeViewState = SelectedTimeViewState(
                selectedTimeModel = modelList,
                selectedTimePieChartData = PieChartData(slices)
            ),
            onMonthModelSelected = {},
            onMonthCategoryClick = {},
            priceViewState = PriceViewState()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
private fun StatisticByTimeTabLayoutEmptyPreview() {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    MotMaterialTheme {
        StatisticByTimeTabLayout(
            scrollBehavior = behavior,
            model = PreviewData.statisticByMonthListPreviewData,
            selectedTimeViewState = SelectedTimeViewState(
                selectedTimeModel = PreviewData.statisticByMonthModelEmptyPreviewData,
            ),
            onMonthModelSelected = {},
            onMonthCategoryClick = {},
            priceViewState = PriceViewState()
        )
    }
}
