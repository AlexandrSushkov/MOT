package dev.nelson.mot.main.presentations.screen.statistic.new_tabs

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.Placeholder
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotOutlinedButton
import dev.nelson.mot.core.ui.PriceText
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.domain.use_case.statistic.StatisticByCategoryPerMonthModel
import dev.nelson.mot.main.presentations.screen.statistic.SelectedCategoryViewState
import dev.nelson.mot.main.presentations.widgets.FABFooter
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder
import dev.nelson.mot.main.presentations.widgets.ListPlaceholderPreview
import dev.nelson.mot.main.presentations.widgets.MotSingleLineText
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticByCategoryTabLayout(
    scrollBehavior: TopAppBarScrollBehavior,
    selectedCategoryViewState: SelectedCategoryViewState,
//    modelList: List<StatisticByCategoryPerMonthModel>,
    priceViewState: PriceViewState
) {
    if (selectedCategoryViewState.selectedTimeModel.paymentToMonth.isEmpty()) {
        ListPlaceholder(modifier = Modifier.fillMaxSize())
    } else {
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
                        LineChart(
                            linesChartData = listOf(selectedCategoryViewState.selectedTimeLineChartData)
                            // Optional properties.
//                    modifier = Modifier.fillMaxSize(),
//                    animation = simpleChartAnimation(),
//                    pointDrawer = FilledCircularPointDrawer(),
//                    lineDrawer = SolidLineDrawer(),
//                    xAxisDrawer = SimpleXAxisDrawer(),
//                    yAxisDrawer = SimpleYAxisDrawer(),
//                    horizontalOffset = 5f,
//                    labels = listOf("label 1" ...)
                        )
//                    Icon(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .size(164.dp)
//                            .padding(16.dp),
//                        imageVector = Icons.Default.StackedBarChart,
//                        contentDescription = ""
//                    )
                    }
                }
//            item {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    MotOutlinedButton(
//                        onClick = { }) {
//                        Text(text = "month")
//                    }
//                    Spacer(modifier = Modifier.padding(8.dp))
//                    MotOutlinedButton(onClick = { /*TODO*/ }) {
//                        Text(text = "year")
//                    }
//                }
//            }
                item {
                    val item = selectedCategoryViewState.selectedTimeModel
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                MotSingleLineText(
                                    modifier = Modifier.weight(1f),
                                    text = item.category?.name ?: "No category",
                                    style = MaterialTheme.typography.titleLarge,
                                )
                                PriceText(
                                    price = item.totalPrice,
                                    priceViewState = priceViewState,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))
                            Column {
                                item.paymentToMonth.forEach {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = it.key.monthText,
                                            style = MaterialTheme.typography.titleMedium,
                                        )
                                        PriceText(
                                            price = it.value.sumOfPaymentsForThisMonth,
                                            priceViewState = priceViewState,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
private fun StatisticByCategoryTabLayoutPreview() {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    MotMaterialTheme {
        StatisticByCategoryTabLayout(
            scrollBehavior = behavior,
            selectedCategoryViewState = SelectedCategoryViewState(
                selectedTimeModel = PreviewData.statisticByCategoryPerMonthModel,
            ),
            priceViewState = PriceViewState()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
private fun StatisticByCategoryTabLayoutEmptyContentPreview() {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    MotMaterialTheme {
        StatisticByCategoryTabLayout(
            scrollBehavior = behavior,
            selectedCategoryViewState = SelectedCategoryViewState(
                selectedTimeModel = PreviewData.statisticByCategoryPerMonthModelEmpty,
            ),
            priceViewState = PriceViewState()
        )
    }
}
