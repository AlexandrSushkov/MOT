package dev.nelson.mot.main.presentations.screen.statistic

import android.app.Activity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.LineChartMot
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.presentations.screen.statistic.tabs.ByCategoryTab
import dev.nelson.mot.main.presentations.screen.statistic.tabs.ByMonthTab
import dev.nelson.mot.main.presentations.screen.statistic.tabs.CurrentMonthTab
import dev.nelson.mot.main.presentations.screen.statistic.tabs.YearsTabLayout
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch

@Composable
fun StatisticScreenExperemental(
    viewModel: StatisticViewModel,
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {}
) {
    val statCurrentMothList by viewModel.statCurrentMothViewState.collectAsState(emptyList())
    val statByMonthList by viewModel.statByMonthListViewState.collectAsState(emptyList())
    val statByYearList by viewModel.statByYearListViewState.collectAsState(emptyList())
    val priceViewState by viewModel.priceViewState.collectAsState(PriceViewState())

    StatisticLayout(
        appBarTitle = appBarTitle,
        appBarNavigationIcon = appBarNavigationIcon,
        statCurrentMothList = statCurrentMothList,
        statByMonthList = statByMonthList,
        statByYearList = statByYearList,
        priceViewState = priceViewState,
        onExpandYearItemClick = { viewModel.onExpandYearClicked(it) },
        onExpandMonthItemClick = { viewModel.onExpandMonthClicked(it) }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun StatisticLayout(
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {},
    statCurrentMothList: List<StatisticByCategoryModel>,
    statByMonthList: List<StatisticByMonthModel>,
    statByYearList: List<StatisticByYearModel>,
    priceViewState: PriceViewState,
    onExpandYearItemClick: (StatisticByYearModel) -> Unit = {},
    onExpandMonthItemClick: (StatisticByMonthModel) -> Unit = {}
) {
    val tabs = MotStatisticTab.getTabs()
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
                navigationIcon = appBarNavigationIcon,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor =  MaterialTheme.colorScheme.secondaryContainer,
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
                .padding(paddingValues = innerPadding),
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

            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                edgePadding = 0.dp,
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
                    is MotStatisticTab.CurrentMonth -> CurrentMonthTab(
                        scrollBehavior = scrollBehavior,
                        model = statCurrentMothList,
                    )

                    is MotStatisticTab.ByMonth -> ByMonthTab(
                        scrollBehavior = scrollBehavior,
                        statByMonthList = statByMonthList,
                        onExpandClick = onExpandMonthItemClick
                    )

                    is MotStatisticTab.ByYear -> YearsTabLayout(
                        scrollBehavior = scrollBehavior,
                        statByYearList = statByYearList,
                        onExpandClick = onExpandYearItemClick
                    )

                    is MotStatisticTab.ByCategory -> ByCategoryTab(scrollBehavior = scrollBehavior)
                }
            }
        }
    }
}

sealed class MotStatisticTab(val title: String) {
    object CurrentMonth : MotStatisticTab("Current Month")
    object ByMonth : MotStatisticTab("By Month")
    object ByYear : MotStatisticTab("By Year")
    object ByCategory : MotStatisticTab("By Category")

    companion object {
        fun getTabs() = listOf(CurrentMonth, ByMonth, ByYear, ByCategory)
    }
}

fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "currentTabWidth"
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "indicatorOffset"
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tab3(scrollBehavior: TopAppBarScrollBehavior) {
    // List items
    val listItems = listOf(
        "test 1 tab 1",
        "test 2 tab 1",
        "test 3 tab 1",
        "test 4 tab 1",
        "test 5 tab 1",
        "test 6 tab 1",
        "test 7 tab 1",
        "test 8 tab 1",
        "test 9 tab 1",
        "test 10 tab 1",
        "test 11 tab 1",
        "test 12 tab 1",
    )

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        state = listState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            listItems.forEach {
                item {
                    Card(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),

                        content = { Text(text = it) }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tab2(scrollBehavior: TopAppBarScrollBehavior) {
    // List items
    val listItems = listOf(
        "test 1 tab 2",
        "test 2 tab 2",
        "test 3 tab 2",
        "test 4 tab 2",
        "test 5 tab 2",
        "test 6 tab 2",
        "test 7 tab 2",
        "test 8 tab 2",
        "test 9 tab 2",
        "test 10 tab 2",
        "test 11 tab 2",
        "test 12 tab 2",
    )

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        state = listState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            listItems.forEach {
                item {
                    Card(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),

                        content = { Text(text = it) }
                    )
                }
            }
        }
    )
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
            statCurrentMothList = PreviewData.statisticByYearModelPreviewData.categoriesModelList,
            statByMonthList = PreviewData.statisticByMonthListPreviewData,
            statByYearList = PreviewData.statisticByYearListPreviewData,
            priceViewState = PreviewData.priceViewState
        )
    }
}
