package dev.nelson.mot.main.presentations.screen.statistic

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.domain.use_case.date_and_time.GetCurrentTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfPreviousMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListByFixedDateRange
import dev.nelson.mot.main.domain.use_case.price.GetPriceViewStateUseCase
import dev.nelson.mot.main.domain.use_case.statistic.GetStatisticByYears
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    getCurrentTimeUseCase: GetCurrentTimeUseCase,
    getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    getStartOfPreviousMonthTimeUseCase: GetStartOfPreviousMonthTimeUseCase,
    getPaymentListByFixedDateRange: GetPaymentListByFixedDateRange,
    getPriceViewStateUseCase: GetPriceViewStateUseCase,
    getStatisticByYears: GetStatisticByYears
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


    val statByYearListViewState
        get() = _statByYearListViewState.asStateFlow()
    private val _statByYearListViewState = MutableStateFlow(emptyList<StatisticByYearModel>())


    init {
//        launch {
//            val currentTime = getCurrentTimeUseCase.execute()
//            val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
//            getPaymentListByFixedDateRange.execute(startOfMonthTime, currentTime).collect {
//                Timber.d(it.toString())
//                val paymentModelsList = it.filterIsInstance<PaymentListItemModel.PaymentItemModel>()
//                val sortedByCategoryPaymentMap =
//                    paymentModelsList.groupBy { paymentModel -> paymentModel.payment.category }
//                sortedByCategoryPaymentMap.forEach { (category, payments) -> Timber.d("spend in this month for ${category?.name ?: "No Category"}: ${payments.sumOf { payment -> payment.payment.cost }}") }
//                Timber.d("spend in this month total: ${paymentModelsList.sumOf { paymentModel -> paymentModel.payment.cost }}")
//                _currentMonthListResult.value = sortedByCategoryPaymentMap
//            }
//        }
//
//        launch {
//            val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
//            val startOfPreviousMonthTime =
//                getStartOfPreviousMonthTimeUseCase.execute(startOfMonthTime)
//            getPaymentListByFixedDateRange.execute(startOfPreviousMonthTime, startOfMonthTime)
//                .collect {
//                    Timber.d(it.toString())
//                    val paymentModelsList =
//                        it.filterIsInstance<PaymentListItemModel.PaymentItemModel>()
//                    val sortedByCategoryPaymentMap =
//                        paymentModelsList.groupBy { paymentModel -> paymentModel.payment.category }
//                    sortedByCategoryPaymentMap.forEach { (category, payments) -> Timber.d("spend in previous month for ${category?.name ?: "No category"}: ${payments.sumOf { payment -> payment.payment.cost }}") }
//                    Timber.d("spend in previous month total: ${paymentModelsList.sumOf { paymentModel -> paymentModel.payment.cost }}")
//                    _previousMonthListResult.value = sortedByCategoryPaymentMap
//                }
//        }

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
    }
}

data class StatisticByYearModel(
    val key: String,
    val year: Int,
    val sumOfCategories: Int,
    val categoriesModelList: List<StatisticByCategoryModel>
) : ExpandableItem {
    override val isExpanded: Boolean = false
}

interface ExpandableItem {
    val isExpanded: Boolean
}

data class StatisticByCategoryModel(
    val category: Category?,
    val sumOfPayments: Int,
    val payments: List<Payment>?
)
