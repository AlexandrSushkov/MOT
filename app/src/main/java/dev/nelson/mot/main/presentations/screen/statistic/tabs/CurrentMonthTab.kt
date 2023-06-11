package dev.nelson.mot.main.presentations.screen.statistic.tabs

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByCategoryModel
import dev.nelson.mot.main.presentations.widgets.MotExpandableItem
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentMonthTab(
    scrollBehavior: TopAppBarScrollBehavior,
    model: List<StatisticByCategoryModel>,
) {
    val expandedState = remember { MutableTransitionState(true) }
    expandedState.targetState = true

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        content = {
            item {
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
                                    Text(text = "Current month")
//                                    Text(text = model.s)
                                }
                            },
                            expandedContent = {
                                Column {
                                    Divider()
                                    model.forEach {
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
                            onExpandButtonClick = {
//                                onExpandClick(model)
                            },
                            expandedState = expandedState
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
fun CurrentMonthTabPreview() {
    MotMaterialTheme {
        CurrentMonthTab(
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                rememberTopAppBarState()
            ),
            model = PreviewData.statisticByYearModelPreviewData.categoriesModelList
        )
    }
}
