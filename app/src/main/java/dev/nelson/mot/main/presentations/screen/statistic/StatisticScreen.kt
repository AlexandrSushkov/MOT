package dev.nelson.mot.main.presentations.screen.statistic

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotNavDrawerIcon
import dev.nelson.mot.core.ui.fundation.getDisplayCornerRadius
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.domain.usecase.statistic.StatisticByCategoryPerMonthModel
import dev.nelson.mot.main.presentations.screen.statistic.newtabs.StatisticByCategoryTabLayout
import dev.nelson.mot.main.presentations.screen.statistic.newtabs.StatisticByTimeTabLayout
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel,
    appBarTitle: String,
    navigationIcon: @Composable () -> Unit = {},
    openPaymentsByCategoryAction: (Int) -> Unit
) {
    val statCurrentMothList by viewModel.statCurrentMothViewState.collectAsState(emptyList())
    val statByMonthList by viewModel.statByMonthListViewState.collectAsState(emptyList())
    val selectedMonthModel by viewModel.selectedMonthModel.collectAsState()
    val statByYearList by viewModel.statByYearListViewState.collectAsState(emptyList())
    val priceViewState by viewModel.priceViewState.collectAsState(PriceViewState())
    val statByCategoryListViewState by viewModel.statByCategoryListViewState.collectAsState(
        emptyList()
    )
    val selectedTimeViewState by viewModel.selectedTimeViewState.collectAsState()
    val selectedCategoryViewState by viewModel.selectedCategoryViewState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

//    /**
//     * Back handler to hide modal bottom sheet
//     */
//    BackHandler(
//        enabled = modalBottomSheetState.isVisible,
//        onBack = { coroutineScope.launch { modalBottomSheetState.hide() } }
//    )

    StatisticLayout(
        appBarTitle = appBarTitle,
//        onNavigationButtonClick = onNavigationButtonClick,
        navigationIcon = navigationIcon,
//        statCurrentMothList = statCurrentMothList,
        statByMonthList = statByMonthList,
        statByCategoryList = statByCategoryListViewState,
        selectedTimeViewState = selectedTimeViewState,
        selectedCategoryViewState = selectedCategoryViewState,
        selectedMonthModel = selectedMonthModel,
        onMonthModelSelected = { viewModel.onMonthModelSelected(it) },
        onMonthCategoryClick = {
//            viewModel.onMonthCategoryClick(it)
            val categoryId = it.category?.id
            val startTime = it.payments?.first()?.dateInMills
            val endTime = it.payments?.last()?.dateInMills
            openPaymentsByCategoryAction.invoke(categoryId ?: -1)
        },
        onCategoryModelSelected = { viewModel.onCategoryModelSelected(it) },
//        statByYearList = statByYearList,
//        priceViewState = priceViewState,
//        onExpandYearItemClick = { viewModel.onExpandYearClicked(it) },
//        onExpandMonthItemClick = { viewModel.onExpandMonthClicked(it) }
        priceViewState = priceViewState
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
private fun StatisticLayout(
    appBarTitle: String,
//    onNavigationButtonClick: () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    selectedMonthModel: StatisticByMonthModel,
    statByMonthList: List<StatisticByMonthModel>,
    statByCategoryList: List<StatisticByCategoryPerMonthModel>,
    selectedTimeViewState: SelectedTimeViewState,
    selectedCategoryViewState: SelectedCategoryViewState,
    onMonthModelSelected: (StatisticByMonthModel) -> Unit,
    onMonthCategoryClick: (StatisticByCategoryModel) -> Unit,
    onCategoryModelSelected: (StatisticByCategoryPerMonthModel) -> Unit,
    priceViewState: PriceViewState
) {
    val statusBarColor = MaterialTheme.colorScheme.secondaryContainer
    val statisticTabs = MotStatistic2Tab.tabs
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState { statisticTabs.size }
    val coroutineScope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val modalBottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    DisposableEffect(systemUiController, statusBarColor) {
        systemUiController.setStatusBarColor(color = statusBarColor)
        onDispose {}
    }

    if (showBottomSheet) {
        /**
         * Back handler to hide modal bottom sheet
         */
        BackHandler(
            enabled = true,
            onBack = {
                Timber.d("bottom sheet BackHandler")
                coroutineScope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
                    if (!modalBottomSheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }
        )

        val displayCornerRadius = getDisplayCornerRadius()

        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = modalBottomSheetState,
            shape = RoundedCornerShape(
                topStart = displayCornerRadius,
                topEnd = displayCornerRadius
            ),
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            if (pagerState.currentPage == 0) {
                ByTimeFilterBottomSheet(
                    model = statByMonthList,
                    selectedMonthModel = selectedTimeViewState.selectedTimeModel
                ) {
                    onMonthModelSelected(it)
                    coroutineScope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
                        if (!modalBottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }
            } else {
                ByCategoryFilterBottomSheet(
                    model = statByCategoryList,
                    onItemSelected = {
                        onCategoryModelSelected(it)
                        coroutineScope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
                            if (!modalBottomSheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    },
                    selectedMonthModel = selectedCategoryViewState.selectedTimeModel
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(text = appBarTitle) },
                navigationIcon = navigationIcon,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { coroutineScope.launch { showBottomSheet = true } },
                content = { Icon(Icons.Default.FilterList, "new payment fab") }
            )
        }
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
                repeat(statisticTabs.size) {
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
                            }
                    )
                },
                tabs = {
                    statisticTabs.forEachIndexed { index, statisticTab ->
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
                            }
                        )
                    }
                }
            )
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                userScrollEnabled = false
            ) { tabId ->
                when (statisticTabs[tabId]) {
                    is MotStatistic2Tab.ByTime -> StatisticByTimeTabLayout(
                        scrollBehavior = scrollBehavior,
                        selectedTimeViewState = selectedTimeViewState,
                        model = statByMonthList,
                        onMonthModelSelected = onMonthModelSelected,
                        onMonthCategoryClick = onMonthCategoryClick,
                        priceViewState = priceViewState
                    )

                    is MotStatistic2Tab.ByCategory -> StatisticByCategoryTabLayout(
                        scrollBehavior = scrollBehavior,
                        selectedCategoryViewState = selectedCategoryViewState,
                        priceViewState = priceViewState
                    )
                }
            }
        }
    }
}

private sealed class MotStatistic2Tab(val title: String) {
    data object ByTime : MotStatistic2Tab("by Time")
    data object ByCategory : MotStatistic2Tab("by Category")

    companion object {
        val tabs
            get() = listOf(ByTime, ByCategory)
    }
}

@MotPreview
@Composable
private fun StaticLayoutPreview() {
    MotMaterialTheme {
        StatisticLayout(
            appBarTitle = "StatisticLayout",
            selectedMonthModel = PreviewData.statisticByMonthModelPreviewData,
            statByMonthList = PreviewData.statisticByMonthListPreviewData,
            statByCategoryList = emptyList(),
            //            onNavigationButtonClick = {},
            navigationIcon = { MotNavDrawerIcon {} },
            onMonthModelSelected = {},
            selectedTimeViewState = SelectedTimeViewState(
                selectedTimeModel = PreviewData.statisticByMonthModelPreviewData
            ),
            selectedCategoryViewState = SelectedCategoryViewState(
                selectedTimeModel = PreviewData.statisticByCategoryPerMonthModel
            ),
            onCategoryModelSelected = {},
            onMonthCategoryClick = {},
            priceViewState = PreviewData.priceViewState
        )
    }
}
