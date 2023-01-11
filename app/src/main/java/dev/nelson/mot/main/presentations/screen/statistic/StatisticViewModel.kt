package dev.nelson.mot.main.presentations.screen.statistic

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.use_case.date_and_time.GetCurrentTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfPreviousMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.execute
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListByDateRange
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    getCurrentTimeUseCase: GetCurrentTimeUseCase,
    getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    getStartOfPreviousMonthTimeUseCase: GetStartOfPreviousMonthTimeUseCase,
    getPaymentListByDateRange: GetPaymentListByDateRange
) : BaseViewModel() {

    /**
     * list of categories with total spendings
     */
    val currentMonthListResult: Flow<Map<Category?, List<Payment>>>
        get() = _currentMonthListResult.asStateFlow()
    private val _currentMonthListResult = MutableStateFlow<Map<Category?, List<Payment>>>(emptyMap())

    val previousMonthListResult: Flow<Map<Category?, List<Payment>>>
        get() = _previousMonthListResult.asStateFlow()
    private val _previousMonthListResult = MutableStateFlow<Map<Category?, List<Payment>>>(emptyMap())

    init {
        launch {
            val currentTime = getCurrentTimeUseCase.execute()
            val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
            getPaymentListByDateRange.execute(startOfMonthTime, currentTime).collect {
                Timber.d(it.toString())
//                val sortedByCategoryPaymentMap = it.groupBy { payment -> payment.category }
//                sortedByCategoryPaymentMap.forEach { (category, payments) -> Timber.d("spend in this month for ${category?.name ?: "No category"}: ${payments.sumOf { payment -> payment.cost }}") }
//                Timber.d("spend in this month total: ${it.sumOf { payment -> payment.cost }}")
//                _currentMonthListResult.value = sortedByCategoryPaymentMap
            }
        }

        launch {
            val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
            val startOfPreviousMonthTime = getStartOfPreviousMonthTimeUseCase.execute(startOfMonthTime)
            getPaymentListByDateRange.execute(startOfPreviousMonthTime, startOfMonthTime).collect {
                Timber.d(it.toString())
//                val sortedByCategoryPaymentMap = it.groupBy { payment -> payment.category }
//                sortedByCategoryPaymentMap.forEach { (category, payments) -> Timber.d("spend in previous month for ${category?.name ?: "No category"}: ${payments.sumOf { payment -> payment.cost }}") }
//                Timber.d("spend in previous month total: ${it.sumOf { payment -> payment.cost }}")
//                _previousMonthListResult.value = sortedByCategoryPaymentMap
            }
        }
    }
}
