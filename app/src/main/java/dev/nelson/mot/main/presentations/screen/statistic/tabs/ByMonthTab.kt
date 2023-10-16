package dev.nelson.mot.main.presentations.screen.statistic.tabs

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByMonthModel
import dev.nelson.mot.main.presentations.widgets.MotExpandableItem
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ByMonthTab(
    scrollBehavior: TopAppBarScrollBehavior,
    statByMonthList: List<StatisticByMonthModel>,
    onExpandClick: (StatisticByMonthModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        content = {
            items(statByMonthList) {
                val expandedState = remember { MutableTransitionState(it.isExpanded) }
                expandedState.targetState = it.isExpanded
                StatByMonthItem(
                    model = it,
                    expandedState = expandedState,
                    onExpandClick = onExpandClick
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
fun ByMonthTabPreview() {
    MotMaterialTheme {
        ByMonthTab(
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                rememberTopAppBarState()
            ),
            statByMonthList = PreviewData.statisticByMonthListPreviewData,
            onExpandClick = {}
        )
    }
}

@Composable
fun StatByMonthItem(
    model: StatisticByMonthModel,
    expandedState: MutableTransitionState<Boolean>,
    onExpandClick: (StatisticByMonthModel) -> Unit = {}
) {
    val calendar = Calendar.getInstance()
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
                        calendar.set(Calendar.MONTH, model.month - 1)
                        Text(
                            text = "${
                            calendar.getDisplayName(
                                Calendar.MONTH,
                                Calendar.LONG,
                                Locale.getDefault()
                            )
                            }/${model.year}"
                        )
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
        }
    }
}

@MotPreview
@Composable
fun StatByMontItemPreview() {
    MaterialTheme {
        StatByMonthItem(
            model = PreviewData.statisticByMonthModelPreviewData,
            expandedState = MutableTransitionState(false)
        )
    }
}
