package dev.nelson.mot.main.presentations.screen.statistic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.core.ui.LineChartMot
import dev.nelson.mot.core.ui.PriceText
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.utils.preview.MotPreview

@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel
) {
    val currentMonth by viewModel.currentMonthListResult.collectAsState(emptyMap())
    val previousMonthList by viewModel.previousMonthListResult.collectAsState(emptyMap())
    val priceViewState by viewModel.priceViewState.collectAsState(PriceViewState())

    StatisticLayout(
        currentMonth,
        previousMonthList,
        priceViewState
    )
}

@Composable
fun StatisticLayout(
    currentMonthList: Map<Category?, List<PaymentListItemModel.PaymentItemModel>>,
    previousMonthList: Map<Category?, List<PaymentListItemModel.PaymentItemModel>>,
    priceViewState: PriceViewState,
) {
    val paymentsForCurrentMonth = mutableListOf<PaymentListItemModel.PaymentItemModel>()
    val paymentsForPreviousMonth =
        mutableListOf<PaymentListItemModel.PaymentItemModel>()
    currentMonthList.entries.forEach {
        paymentsForCurrentMonth.addAll(it.value)
    }
    previousMonthList.entries.forEach {
        paymentsForPreviousMonth.addAll(it.value)
    }
    val sumForCurrentMonth = paymentsForCurrentMonth.sumOf { it.payment.cost }
    val sumForPreviousMonth = paymentsForPreviousMonth.sumOf { it.payment.cost }

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding),
        ) {
            item {
                StatisticContent()
            }
            item {

                Row {
                    Text(
                        text = "Current month total: ",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    PriceText(
                        price = sumForCurrentMonth,
                        priceViewState = priceViewState
                    )
                }
                Divider()
                Text(
                    text = "Current month:",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            currentMonthList.keys.forEach {
                item {
                    Row {
                        Text(
                            text = it?.name?.let { name -> "$name:" } ?: "NO category:",
                            style = MaterialTheme.typography.labelLarge,
                        )
                        PriceText(
                            price = currentMonthList[it].let { pl ->
                                pl?.sumOf { payment -> payment.payment.cost } ?: 0
                            },
                            priceViewState = priceViewState
                        )
                    }

                }
            }
            item {
                Divider()
            }
            item {
                Row {
                    Text(
                        text = "Previous month total: ",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    PriceText(
                        price = sumForPreviousMonth,
                        priceViewState = priceViewState
                    )
                }
                Divider()
                Text(
                    text = "Previous month by categories:",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            previousMonthList.keys.forEach {
                item {
                    Row {
                        Text(
                            text = it?.name?.let { name -> "$name:" } ?: "NO category:",
                            style = MaterialTheme.typography.labelLarge,
                        )
                        PriceText(
                            price = previousMonthList[it].let { pl ->
                                pl?.sumOf { paymentModel -> paymentModel.payment.cost } ?: 0
                            },
                            priceViewState = priceViewState
                        )
                    }

                }
            }
        }
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
            currentMonthList = emptyMap(),
            previousMonthList = emptyMap(),
            priceViewState = PriceViewState()
        )
    }
}
