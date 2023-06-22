package dev.nelson.mot.main.presentations.screen.statistic.new_tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotOutlinedButton
import dev.nelson.mot.main.domain.use_case.statistic.StatisticByCategoryPerMonthModel
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticByCategoryTabLayout(
    scrollBehavior: TopAppBarScrollBehavior,
    modelList: List<StatisticByCategoryPerMonthModel>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        content = {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(164.dp)
                            .padding(16.dp),
                        imageVector = Icons.Default.StackedBarChart,
                        contentDescription = ""
                    )
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
                val item = modelList.first()
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
                                    text = item.category?.name ?: "No category",
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            },
                            trailingContent = {
//                                Text(
//                                    text = "${item.}",
//                                    style = MaterialTheme.typography.titleLarge,
//
//                                    )

                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                        )
                        Divider()
                        Column {
                            item.paymentToMonth.forEach {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${it.key.month}/${it.key.year}",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    Text(
                                        text = it.value.sumOfPaymentsForThisMonth.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
private fun StatisticByCategoryTabLayoutPreview() {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    MotMaterialTheme {
        StatisticByCategoryTabLayout(
            scrollBehavior = behavior,
            modelList = PreviewData.generateStatisticByCategoryPerMonthModelListPreviewProvider,
        )
    }
}
