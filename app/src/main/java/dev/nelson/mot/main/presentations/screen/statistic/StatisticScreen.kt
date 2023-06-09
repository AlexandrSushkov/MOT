package dev.nelson.mot.main.presentations.screen.statistic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.core.ui.LineChartMot
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.presentations.widgets.ExpandableContent
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview

@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel,
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {}
) {
    val currentMonthList by viewModel.currentMonthListResult.collectAsState(emptyMap())
    val previousMonthList by viewModel.previousMonthListResult.collectAsState(emptyMap())
    val priceViewState by viewModel.priceViewState.collectAsState(PriceViewState())
    val statByYearList by viewModel.statByYearListViewState.collectAsState(emptyList<StatisticByYearModel>())

    StatisticLayout(
        appBarTitle = appBarTitle,
        appBarNavigationIcon = appBarNavigationIcon,
        statByYearList = statByYearList,
        priceViewState = priceViewState
    )
}

@Composable
fun StatisticLayout(
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {},
    statByYearList: List<StatisticByYearModel>,
    priceViewState: PriceViewState,
) {
    val paymentsForCurrentMonth = mutableListOf<PaymentListItemModel.PaymentItemModel>()
    val paymentsForPreviousMonth = mutableListOf<PaymentListItemModel.PaymentItemModel>()

//    currentMonthList.entries.forEach { paymentsForCurrentMonth.addAll(it.value) }
//    previousMonthList.entries.forEach { paymentsForPreviousMonth.addAll(it.value) }
//
//    val sumForCurrentMonth = paymentsForCurrentMonth.sumOf { it.payment.cost }
//    val sumForPreviousMonth = paymentsForPreviousMonth.sumOf { it.payment.cost }

    val statisticListScrollingState = rememberLazyListState()

    val isContentScrolling =
        remember { derivedStateOf { statisticListScrollingState.firstVisibleItemIndex != 0 } }

    Scaffold(
        topBar = {
            MotTopAppBar(
                appBarTitle = appBarTitle,
                navigationIcon = appBarNavigationIcon,
                isContentScrolling = isContentScrolling.value
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = statisticListScrollingState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding),
        ) {
            statByYearList.forEach {
                item {
                    StatByYearItem(model = it)
                }
            }
        }
    }
}

@Composable
fun StatByYearItem(
    model: StatisticByYearModel
) {
    val isExp  = model as? ExpandableItem
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
//            ExpandableContent(text = , transitionState = ) {
//
//            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${model.year}  - ${isExp?.isExpanded ?: "not expandable"}")
                Text(text = model.sumOfCategories.toString())
            }
            model.categoriesModelList.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = it.category?.name ?: "No category")
                    Text(text = it.sumOfPayments.toString())
                }
            }
        }
    }
}

@MotPreview
@Composable
fun StatByYearItem() {
    MaterialTheme {
        StatByYearItem(PreviewData.statisticByYearModelPreviewData)
    }
}

@Composable
fun StatisticContent() {
    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
        LineChartMot(items = listOf(0.2f, 0.5f, 0.1f, 0.3f))
    }
}

@MotPreview
@Composable
private fun StatisticLayoutPreview() {
    MotMaterialTheme {
        StatisticLayout(
            appBarTitle = "Statistic",
            appBarNavigationIcon = {},
            statByYearList = PreviewData.statisticByYearListPreviewData,
            priceViewState = PriceViewState()
        )
    }
}
