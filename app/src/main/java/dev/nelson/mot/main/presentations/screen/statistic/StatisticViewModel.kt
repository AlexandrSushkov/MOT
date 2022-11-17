package dev.nelson.mot.main.presentations.screen.statistic

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.use_case.date_and_time.GetCurrentTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
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
    getPaymentListByDateRange: GetPaymentListByDateRange
) : BaseViewModel() {

    /**
     * list of categories with total spendings
     */
    val paymentListResult: Flow<Map<Category?, List<Payment>>>
        get() = _paymentList.asStateFlow()
    private val _paymentList = MutableStateFlow<Map<Category?,List<Payment>>>(emptyMap())

    init {
        viewModelScope.launch {
            val currentTime = getCurrentTimeUseCase.execute()
            val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
            getPaymentListByDateRange.execute(startOfMonthTime, currentTime).collect {
                Timber.d(it.toString())
                val sortedPaymentMap = it.groupBy { payment -> payment.category }
                sortedPaymentMap.forEach { (category, payments) -> Timber.d("spend in this month for ${category?.name ?: "No category"}: ${payments.sumOf { payment -> payment.cost }}") }
                Timber.d("spend in this month total: ${it.sumOf { payment -> payment.cost }}")
                _paymentList.value = sortedPaymentMap
            }
        }
    }
}