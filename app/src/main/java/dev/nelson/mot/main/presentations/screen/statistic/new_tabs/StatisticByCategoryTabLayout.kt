package dev.nelson.mot.main.presentations.screen.statistic.new_tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.PriceText
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.presentations.screen.statistic.SelectedCategoryViewState
import dev.nelson.mot.main.presentations.widgets.FABFooter
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder
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
    Surface(modifier = Modifier.fillMaxSize()) {
        if (selectedCategoryViewState.selectedTimeModel.paymentToMonth.isEmpty()) {
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
                        MotLineChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            selectedCategoryViewState = selectedCategoryViewState,
                        )
//                        Row {
//                            Text(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 16.dp),
//                                textAlign = TextAlign.Center,
//                                text = "start date - end date"
//                            )
//                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MotSingleLineText(
                                modifier = Modifier.weight(1f),
                                text = selectedCategoryViewState.selectedTimeModel.category?.name
                                    ?: "No category",
                                style = MaterialTheme.typography.titleLarge,
                            )
                            PriceText(
                                price = selectedCategoryViewState.selectedTimeModel.totalPrice,
                                priceViewState = priceViewState,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    content = {
                        item {
                            selectedCategoryViewState.selectedTimeModel
                                .paymentToMonth.forEach {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        item {
                            FABFooter()
                        }
                    }
                )
            }
        }
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
            priceViewState = PreviewData.priceViewState
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
            priceViewState = PreviewData.priceViewState
        )
    }
}
