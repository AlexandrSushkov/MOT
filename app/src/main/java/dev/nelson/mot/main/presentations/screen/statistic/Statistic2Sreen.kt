package dev.nelson.mot.main.presentations.screen.statistic

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotNavBackIcon
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.presentations.screen.statistic.new_tabs.StatisticByCategoryTabLayout
import dev.nelson.mot.main.presentations.screen.statistic.new_tabs.StatisticByTimeTabLayout
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch

@Composable
fun Statistic2Screen(
    viewModel: StatisticViewModel,
    appBarTitle: String,
    onNavigationButtonClick: () -> Unit = {}
) {
    val statCurrentMothList by viewModel.statCurrentMothViewState.collectAsState(emptyList())
    val statByMonthList by viewModel.statByMonthListViewState.collectAsState(emptyList())
    val selectedMonthModel by viewModel.selectedMonthModel.collectAsState()
    val statByYearList by viewModel.statByYearListViewState.collectAsState(emptyList())
    val priceViewState by viewModel.priceViewState.collectAsState(PriceViewState())

    Statistic2Layout(
        appBarTitle = appBarTitle,
        onNavigationButtonClick = onNavigationButtonClick,
//        statCurrentMothList = statCurrentMothList,
        statByMonthList = statByMonthList,
        selectedMonthModel = selectedMonthModel,
//        statByYearList = statByYearList,
//        priceViewState = priceViewState,
//        onExpandYearItemClick = { viewModel.onExpandYearClicked(it) },
//        onExpandMonthItemClick = { viewModel.onExpandMonthClicked(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun Statistic2Layout(
    appBarTitle: String,
    onNavigationButtonClick: () -> Unit,
    selectedMonthModel: StatisticByMonthModel,
    statByMonthList: List<StatisticByMonthModel>,
) {

    val tabs = MotStatistic2Tab.tabs
    val pagerState = rememberPagerState(0)
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            val appBarColor = MaterialTheme.colorScheme.secondaryContainer

            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(text = appBarTitle) },
                navigationIcon = { MotNavBackIcon(onClick = onNavigationButtonClick) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ).also {
                    val view = LocalView.current
                    if (!view.isInEditMode) {
                        val window = (view.context as Activity).window
                        window.statusBarColor = appBarColor.toArgb()
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            val textWidth = remember { mutableStateOf(0) }

            val density = LocalDensity.current
            val tabWidths = remember {
                val tabWidthStateList = mutableStateListOf<Dp>()
                repeat(tabs.size) {
                    tabWidthStateList.add(0.dp)
                }
                tabWidthStateList
            }

            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .customTabIndicatorOffset(
                                currentTabPosition = tabPositions[pagerState.currentPage],
                                tabWidth = tabWidths[pagerState.currentPage]
                            )
                            .height(4.dp)
                            .width(with(LocalDensity.current) { textWidth.value.toDp() })
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                            .onGloballyPositioned { coordinates ->
                                textWidth.value = coordinates.size.width
                            },
                    )
                },
//                divider = {
                // default divider in not cover the whole width if there is less tabs than screen width
//                },
                tabs = {
                    tabs.forEachIndexed { index, statisticTab ->
                        Tab(
                            text = {
                                Text(
                                    text = statisticTab.title,
                                    onTextLayout = { textLayoutResult ->
                                        tabWidths[index] =
                                            with(density) {
                                                textLayoutResult.size.width
                                                    .toDp()
                                            }
                                    }
                                )
                            },
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                        )
                    }
                }
            )
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                pageCount = tabs.size,
                state = pagerState,
            ) { tabId ->
                when (tabs[tabId]) {
                    is MotStatistic2Tab.ByTime -> StatisticByTimeTabLayout(
                        scrollBehavior = scrollBehavior,
                        selectedMonthModel = selectedMonthModel,
                        model = statByMonthList,
                    )

                    is MotStatistic2Tab.ByCategory -> StatisticByCategoryTabLayout(
                        scrollBehavior = scrollBehavior,
                    )
                }
            }
        }
    }
}

private sealed class MotStatistic2Tab(val title: String) {
    object ByTime : MotStatistic2Tab("by Time")
    object ByCategory : MotStatistic2Tab("by Category")

    companion object {
        val tabs
            get() = listOf(ByTime, ByCategory)
    }
}

@MotPreview
@Composable
private fun Static2LayoutPreview() {
    MotMaterialTheme {
        Statistic2Layout(
            appBarTitle = "Statistic2Layout",
            selectedMonthModel = PreviewData.statisticByMonthModelPreviewData,
            statByMonthList = PreviewData.statisticByMonthListPreviewData,
            onNavigationButtonClick = {}
        )
    }
}
