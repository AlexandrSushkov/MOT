package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepository
import dev.nelson.mot.main.domain.use_case.date_and_time.FormatTimeUseCase
import dev.nelson.mot.main.util.SortingOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * By default it returns list of [Payment] for current month sorted by the time, starting from the latest added [Payment].
 */
class GetPaymentListByDateRange @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val formatTimeUseCase: FormatTimeUseCase
) {

    fun execute(startTime: Long, endTime: Long? = null, order: SortingOrder = SortingOrder.Ascending): Flow<List<Payment>> {
        val paymentsWithCategoryList = endTime?.let { paymentRepository.getPaymentsWithCategoryByDateRangeOrdered(startTime, it, order) }
            ?: paymentRepository.getPaymentsWithCategoryByDateRangeOrdered(startTime, order)
        return paymentsWithCategoryList
            .map { it.toPaymentList() }
            .map { it.formatDate() }
    }

    /**
     * transform epoch mills into string date according to time system zone
     */
    private fun List<Payment>.formatDate(): List<Payment> {
        return this.map { payment -> payment.copyWith(date = formatTimeUseCase.execute(payment.dateInMills)) }
    }

}