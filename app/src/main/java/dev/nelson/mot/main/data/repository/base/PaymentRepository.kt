package dev.nelson.mot.main.data.repository.base

import dev.nelson.mot.db.model.payment.PaymentEntity
import dev.nelson.mot.db.model.paymentjoin.PaymentWithCategory
import dev.nelson.mot.main.util.SortingOrder
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {

    /**
     * Get payment
     *
     * @param paymentId id of the payment
     * @return [PaymentWithCategory]
     */
    fun getPayment(paymentId: Int): Flow<PaymentWithCategory>

    /**
     * Get payments WITHOUT end date used on Payments list screen to listen for the updates when new payment is added.
     */
    fun getPaymentsWithCategoryByDateRangeOrdered(startTime: Long, order: SortingOrder, categoryId: Int?): Flow<List<PaymentWithCategory>>

    /**
     * Get payments WITH end date used to get payments in a particular time period.
     */
    fun getPaymentsWithCategoryByDateRangeOrdered(startTime: Long, endTime: Long, order: SortingOrder): Flow<List<PaymentWithCategory>>

    suspend fun addPayment(paymentEntity: PaymentEntity)

    suspend fun updatePayment(paymentEntity: PaymentEntity)

    suspend fun updatePayments(paymentEntityList: List<PaymentEntity>)

    suspend fun deletePayment(paymentEntity: PaymentEntity)

    suspend fun deletePayments(paymentEntityList: List<PaymentEntity>)
}
