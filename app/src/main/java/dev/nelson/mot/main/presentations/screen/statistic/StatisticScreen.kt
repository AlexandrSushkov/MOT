package dev.nelson.mot.main.presentations.screen.statistic

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import dev.nelson.mot.main.presentations.widgets.MotExpandableItem
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview

@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel,
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {}
) {
    val statByYearList by viewModel.statByYearListViewState.collectAsState(emptyList())
    val priceViewState by viewModel.priceViewState.collectAsState(PriceViewState())

    StatisticLayout(
        appBarTitle = appBarTitle,
        appBarNavigationIcon = appBarNavigationIcon,
        statByYearList = statByYearList,
        priceViewState = priceViewState,
        onExpandClick = { viewModel.onExpandClicked(it) }
    )
}

@Composable
fun StatisticLayout(
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {},
    statByYearList: List<StatisticByYearModel>,
    priceViewState: PriceViewState,
    onExpandClick: (StatisticByYearModel) -> Unit = {}
) {
    val statisticListScrollingState = rememberLazyListState()

    Scaffold(
        topBar = {
            MotTopAppBar(
                appBarTitle = appBarTitle,
                navigationIcon = appBarNavigationIcon,
                screenContentScrollingState = statisticListScrollingState
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
                    val expandedState = remember { MutableTransitionState(it.isExpanded) }
                    expandedState.targetState = it.isExpanded
                    StatByYearItem(
                        model = it,
                        expandedState = expandedState,
                        onExpandClick = onExpandClick
                    )
                }
            }
        }
    }
}

@Composable
fun StatByYearItem(
    model: StatisticByYearModel,
    expandedState: MutableTransitionState<Boolean>,
    onExpandClick: (StatisticByYearModel) -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            MotExpandableItem(
                titleContent = {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${model.year}")
                        Text(text = model.sumOfCategories.toString())
                    }
                },
                expandedContent = {
                    Column {
                        Divider()
                        model.categoriesModelList.forEach {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = it.category?.name ?: "No category")
                                Text(text = it.sumOfPayments.toString())
                            }
                        }
                    }
                },
                expandButtonIcon = Icons.Default.KeyboardArrowUp,
                onExpandButtonClick = { onExpandClick(model) },
                expandedState = expandedState
            )

//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = "${model.year}")
//                Text(text = model.sumOfCategories.toString())
//            }
//            model.categoriesModelList.forEach {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(text = it.category?.name ?: "No category")
//                    Text(text = it.sumOfPayments.toString())
//                }
//            }
        }
    }
}

@MotPreview
@Composable
fun StatByYearItem() {
    MaterialTheme {
        StatByYearItem(
            PreviewData.statisticByYearModelPreviewData,
            expandedState = MutableTransitionState(false)
        )
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
