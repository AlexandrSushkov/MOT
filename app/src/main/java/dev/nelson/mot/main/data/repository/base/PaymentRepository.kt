package dev.nelson.mot.main.data.repository.base

import dev.nelson.mot.db.model.payment.PaymentEntity
import dev.nelson.mot.db.model.paymentjoin.PaymentWithCategory
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {

    /**
     * Get payment
     *
     * @param paymentId id of the payment
     * @return [PaymentWithCategory]
     */
    fun getPayment(paymentId: Int): Flow<PaymentWithCategory>

    suspend fun getAllPayments(): List<PaymentEntity>

    /**
     * Get payments WITHOUT end date used on Payments list screen to listen for the updates when new payment is added.
     * @param isAsc true if ascending order is needed, false otherwise.
     */
    fun getPaymentsWithCategoryByCategoryIdNoFixedDateRange(
        startTime: Long,
        categoryId: Int? = null,
        isAsc: Boolean = false
    ): Flow<List<PaymentWithCategory>>

    /**
     * Get payments WITH end date used to get payments in a particular time period.
     * @param isAsc true if ascending order is needed, false otherwise.
     */
    fun getPaymentsWithCategoryByFixedDateRange(
        startTime: Long,
        endTime: Long,
        isAsc: Boolean = false
    ): Flow<List<PaymentWithCategory>>

    suspend fun addPayment(paymentEntity: PaymentEntity)

    suspend fun updatePayment(paymentEntity: PaymentEntity)

    suspend fun updatePayments(paymentEntityList: List<PaymentEntity>)

    suspend fun deletePayment(paymentEntity: PaymentEntity)

    suspend fun deletePayments(paymentEntityList: List<PaymentEntity>)
}
