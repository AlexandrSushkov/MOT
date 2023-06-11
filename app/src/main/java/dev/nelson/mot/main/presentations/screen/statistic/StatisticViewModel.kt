package dev.nelson.mot.main.presentations.screen.statistic

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.date_and_time.GetCurrentTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfPreviousMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListByFixedDateRange
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListNoFixedDateRange
import dev.nelson.mot.main.domain.use_case.price.GetPriceViewStateUseCase
import dev.nelson.mot.main.domain.use_case.statistic.GetStatisticByYears
import dev.nelson.mot.main.domain.use_case.statistic.GetStatisticForCurrentMonthUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    getCurrentTimeUseCase: GetCurrentTimeUseCase,
    getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    getStartOfPreviousMonthTimeUseCase: GetStartOfPreviousMonthTimeUseCase,
    getPaymentListByFixedDateRange: GetPaymentListByFixedDateRange,
    getPriceViewStateUseCase: GetPriceViewStateUseCase,
    getStatisticByYears: GetStatisticByYears,
    private val getPaymentListNoFixedDateRange: GetPaymentListNoFixedDateRange,
    private val getStatisticForCurrentMonthUseCase: GetStatisticForCurrentMonthUseCase,

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
            getStatisticByYears.execute().collect {
                _statByYearListViewState.value = it
            }
        }

        launch {
            val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
            // no end date. otherwise newly added payments won't be shown.
            getStatisticForCurrentMonthUseCase.execute(startOfMonthTime).collect {
                _statCurrentMothViewState.value = it
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
    fun onExpandClicked(statisticByYearModel: StatisticByYearModel) {
        val oldList = _statByYearListViewState.value
        val oldItemIndex = oldList.indexOf(statisticByYearModel)
        val oldItem = oldList[oldItemIndex]
        val newItem = oldItem.copy(isExpanded = !statisticByYearModel.isExpanded)
        val newList = mutableListOf<StatisticByYearModel>().apply {
            this.addAll(oldList)
            this[oldItemIndex] = newItem
        }.toList()
        _statByYearListViewState.value = newList
    }
}

data class StatisticByYearModel(
    val key: String,
    val year: Int,
    val sumOfCategories: Int,
    val isExpanded: Boolean = false,
    val categoriesModelList: List<StatisticByCategoryModel>
)

data class StatisticByCategoryModel(
    val key: String,
    val category: Category?,
    val sumOfPayments: Int,
    val payments: List<Payment>?
)
