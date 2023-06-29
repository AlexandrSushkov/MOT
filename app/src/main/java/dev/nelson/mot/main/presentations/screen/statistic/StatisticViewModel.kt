package dev.nelson.mot.main.presentations.screen.statistic

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.piechart.PieChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.date_and_time.GetCurrentTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfPreviousMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListByFixedDateRangeUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListNoFixedDateRange
import dev.nelson.mot.main.domain.use_case.price.GetPriceViewStateUseCase
import dev.nelson.mot.main.domain.use_case.statistic.GetStatisticByCategoryUseCase
import dev.nelson.mot.main.domain.use_case.statistic.GetStatisticByMonthUseCase
import dev.nelson.mot.main.domain.use_case.statistic.GetStatisticByYearsUseCase
import dev.nelson.mot.main.domain.use_case.statistic.GetStatisticForCurrentMonthUseCase
import dev.nelson.mot.main.domain.use_case.statistic.StatisticByCategoryPerMonthModel
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.StringUtils
import dev.theme.lightChartColors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    getCurrentTimeUseCase: GetCurrentTimeUseCase,
    getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    getStartOfPreviousMonthTimeUseCase: GetStartOfPreviousMonthTimeUseCase,
    getPaymentListByFixedDateRangeUseCase: GetPaymentListByFixedDateRangeUseCase,
    getPriceViewStateUseCase: GetPriceViewStateUseCase,
    getStatisticByYearsUseCase: GetStatisticByYearsUseCase,
    private val getPaymentListNoFixedDateRange: GetPaymentListNoFixedDateRange,
    private val getStatisticForCurrentMonthUseCase: GetStatisticForCurrentMonthUseCase,
    private val getStatisticByMonthUseCase: GetStatisticByMonthUseCase,
    private val getStatisticByCategoryUseCase: GetStatisticByCategoryUseCase,

    ) : BaseViewModel() {

    /**
     * list of categories with total spendings
     */
    val currentMonthListResult: Flow<Map<Category?, List<PaymentListItemModel.PaymentItemModel>>>
        get() = _currentMonthListResult.asStateFlow()
    private val _currentMonthListResult =
        MutableStateFlow<Map<Category?, List<PaymentListItemModel.PaymentItemModel>>>(emptyMap())

    val previousMonthListResult: Flow<Map<Category?, List<PaymentListItemModel.PaymentItemModel>>>
        get() = _previousMonthListResult.asStateFlow()
    private val _previousMonthListResult =
        MutableStateFlow<Map<Category?, List<PaymentListItemModel.PaymentItemModel>>>(emptyMap())

    val priceViewState
        get() = _priceViewState.asStateFlow()
    private val _priceViewState = MutableStateFlow(PriceViewState())

    val statCurrentMothViewState
        get() = _statCurrentMothViewState.asStateFlow()
    private val _statCurrentMothViewState =
        MutableStateFlow(emptyList<StatisticByCategoryModel>())

    val statByMonthListViewState
        get() = _statByMonthListViewState.asStateFlow()
    private val _statByMonthListViewState = MutableStateFlow(emptyList<StatisticByMonthModel>())

    val statByCategoryListViewState
        get() = _statByCategoryListViewState.asStateFlow()
    private val _statByCategoryListViewState =
        MutableStateFlow(emptyList<StatisticByCategoryPerMonthModel>())

    val selectedCategoryViewState
        get() = _selectedCategoryViewState.asStateFlow()
    private val _selectedCategoryViewState = MutableStateFlow(SelectedCategoryViewState())

    val selectedTimeViewState
        get() = _selectedTimeViewState.asStateFlow()
    private val _selectedTimeViewState = MutableStateFlow(SelectedTimeViewState())

    val selectedMonthModel
        get() = _selectedMonthModel.asStateFlow()
    private val _selectedMonthModel = MutableStateFlow(StatisticByMonthModel())

    val statByYearListViewState
        get() = _statByYearListViewState.asStateFlow()
    private val _statByYearListViewState = MutableStateFlow(emptyList<StatisticByYearModel>())


    init {
        launch {
            getPriceViewStateUseCase.execute().collect {
                _priceViewState.value = it
            }
        }

        launch {
            val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
            // no end date. otherwise newly added payments won't be shown.
            getStatisticForCurrentMonthUseCase.execute(startOfMonthTime).collect {
                _statCurrentMothViewState.value = it
            }
        }

        launch {
            getStatisticByYearsUseCase.execute().collect {
                _statByYearListViewState.value = it
            }
        }

        launch {
            getStatisticByMonthUseCase.execute().collect {
                _statByMonthListViewState.value = it
                onMonthModelSelected(it.first())
            }
        }

        launch {
            getStatisticByCategoryUseCase.execute()
                .collect { statisticByCategoryPerMonthModelList ->
                    _statByCategoryListViewState.value = statisticByCategoryPerMonthModelList
                    onCategoryModelSelected(statisticByCategoryPerMonthModelList.first())
                }
        }
    }

    /**
     * get item form old list
     * copy item with new data
     * copy new list
     * replace old item with new item
     * update list state with new list
     */
    fun onExpandYearClicked(statisticByYearModel: StatisticByYearModel) {
        _statByYearListViewState.update {
            val oldList = _statByYearListViewState.value
            val oldItemIndex = oldList.indexOf(statisticByYearModel)
            val oldItem = oldList[oldItemIndex]
            val newItem = oldItem.copy(isExpanded = !statisticByYearModel.isExpanded)
            mutableListOf<StatisticByYearModel>().apply {
                this.addAll(oldList)
                this[oldItemIndex] = newItem
            }.toList()
        }
    }

    fun onExpandMonthClicked(statisticByMonthModel: StatisticByMonthModel) {
        _statByMonthListViewState.update {
            val oldList = _statByMonthListViewState.value
            val oldItemIndex = oldList.indexOf(statisticByMonthModel)
            val oldItem = oldList[oldItemIndex]
            val newItem = oldItem.copy(isExpanded = !statisticByMonthModel.isExpanded)
            mutableListOf<StatisticByMonthModel>().apply {
                this.addAll(oldList)
                this[oldItemIndex] = newItem
            }.toList()
        }
    }

    fun onMonthModelSelected(model: StatisticByMonthModel) {
        _selectedTimeViewState.update {
            val pieSlices = model.categoriesModelList
                .mapIndexed { index, item ->
                    val colorIndex = index % lightChartColors.size
                    PieChartData.Slice(
                        item.sumOfPayments.toFloat(),
                        lightChartColors[colorIndex]
                    )
//                }
//                .map {
//                PieChartData.Slice(
//                    it.sumOfPayments.toFloat(),
//                    generateRandomColor()
//                )
                }
            SelectedTimeViewState(
                selectedTimeModel = model,
                selectedTimePieChartData = PieChartData(pieSlices)
            )
        }
    }

    fun onCategoryModelSelected(model: StatisticByCategoryPerMonthModel) {
        _selectedCategoryViewState.update {
            val points = model
                .paymentToMonth
                .map { paymentsByMothModel ->
                    LineChartData.Point(
                        paymentsByMothModel.value.sumOfPaymentsForThisMonth.toFloat(),
                        ""// label - x axis text
                    )
                }
            SelectedCategoryViewState(
                model,
                LineChartData(points, lineDrawer = SolidLineDrawer(2.dp, Color.Blue))
            )
        }
    }

    private fun generateRandomColor(): Color {
        val i = (0..8).random()
        return lightChartColors[i]
//        val random = Random.Default
//        val red = random.nextInt(256)
//        val green = random.nextInt(256)
//        val blue = random.nextInt(256)
//        return Color(red, green, blue)
    }

    fun onMonthCategoryClick(model: StatisticByCategoryModel) {

    }
}

data class SelectedTimeViewState(
    val selectedTimeModel: StatisticByMonthModel = StatisticByMonthModel(),
    val selectedTimePieChartData: PieChartData = PieChartData(emptyList())
)

data class SelectedCategoryViewState(
    val selectedTimeModel: StatisticByCategoryPerMonthModel = StatisticByCategoryPerMonthModel(
        key = StringUtils.EMPTY,
        paymentToMonth = emptyMap()
    ),
    val selectedTimeLineChartData: LineChartData = LineChartData(
        emptyList(),
        lineDrawer = SolidLineDrawer()
    )
)

data class StatisticByYearModel(
    val key: String,
    val year: Int,
    val sumOfCategories: Int,
    val isExpanded: Boolean = false,
    val categoriesModelList: List<StatisticByCategoryModel>
)

data class StatisticByMonthModel(
    val key: String = StringUtils.EMPTY,
    val monthText: String = StringUtils.EMPTY,
    val month: Int = 0,
    val year: Int = 0,
    val sumOfCategories: Int = 0,
    val isExpanded: Boolean = false,
    val categoriesModelList: List<StatisticByCategoryModel> = emptyList()
)

data class StatisticByCategoryModel(
    val key: String,
    val category: Category?,
    val sumOfPayments: Int,
    val percentage: Double,
    val payments: List<Payment>?
)
