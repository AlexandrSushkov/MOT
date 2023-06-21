package dev.nelson.mot.main.presentations.screen.statistic.new_tabs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.StackedBarChart
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotOutlinedButton
import dev.nelson.mot.core.ui.MotSwitch
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByMonthModel
import dev.nelson.mot.main.presentations.widgets.MotModalBottomSheetLayout
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun StatisticByTimeTabLayout(
    scrollBehavior: TopAppBarScrollBehavior,
    selectedMonthModel: StatisticByMonthModel,
    model: List<StatisticByMonthModel>,
    onMonthModelSelected: (StatisticByMonthModel) -> Unit,
) {
    val switchState = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val layColumnState = rememberLazyListState()

    /**
     * Back handler to hide modal bottom sheet
     */
    BackHandler(
        enabled = modalBottomSheetState.isVisible,
        onBack = { coroutineScope.launch { modalBottomSheetState.hide() } }
    )

    MotModalBottomSheetLayout(
        sheetContent = {
            LazyColumn(
                state = layColumnState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(model) {
                    ListItem(
                        modifier = Modifier.clickable {
                            onMonthModelSelected(it)
                            coroutineScope.launch { modalBottomSheetState.hide() }
                        },
                        headlineContent = { Text(text = "${it.month}/ ${it.year}") },
                        trailingContent = {
                            if (it == selectedMonthModel) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = ""
                                )
                            }
                        }
                    )
                }
            }
        },
        sheetState = modalBottomSheetState
    ) {
        Column {
//            MotSwitch(
//                checked = false,
//                onCheckedChange = { switchState.value = it },
//                checkedStateIcon = Icons.Default.PieChart,
//                uncheckedStateIcon = Icons.Default.StackedBarChart,
//            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    modifier = Modifier
//                        .align(Alignment.End)
                        .padding(8.dp),

                    onClick = {
                        coroutineScope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "save icon"
                    )
                }
            }
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
                    imageVector = Icons.Default.PieChart,
                    contentDescription = ""
                )
            }
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                MotOutlinedButton(
//                    onClick = { }) {
//                    Text(text = "month")
//                }
//                Spacer(modifier = Modifier.padding(8.dp))
//                MotOutlinedButton(onClick = { /*TODO*/ }) {
//                    Text(text = "year")
//                }
//            }
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
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Text(text = "${selectedMonthModel.month}/${selectedMonthModel.year}")
//                                }
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
                                        ){
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
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@MotPreview
@Composable
private fun StatisticByTimeTabLayoutPreview() {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    MotMaterialTheme {
        StatisticByTimeTabLayout(
            scrollBehavior = behavior,
            selectedMonthModel = PreviewData.statisticByMonthModelPreviewData,
            model = PreviewData.statisticByMonthListPreviewData,
            onMonthModelSelected = {}
        )
    }
}
