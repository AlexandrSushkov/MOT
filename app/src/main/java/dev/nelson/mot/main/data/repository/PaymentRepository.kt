package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepository @Inject constructor(private val motDatabase: MotDatabase) {

    //GET
    /**
     * Get payment
     *
     * @param paymentId id of the payment
     * @return [PaymentWithCategory]
     */
    fun getPayment(paymentId: Int): Flow<PaymentWithCategory> = motDatabase
        .paymentDao()
        .getPaymentById(paymentId)

//    suspend fun getAllPaymentsWithCategoryOrderDateDesc(): List<PaymentWithCategory> = motDatabase
//        .paymentDao()
//        .getAllPaymentsWithCategoryOrderDateDescCor()

    suspend fun getAllPaymentsWithCategory(): List<PaymentWithCategory> = motDatabase
        .paymentDao()
        .getAllPaymentsWithCategoryCor()

    fun getAllPaymentsWithCategoryOrderDateDescFlow(): Flow<List<PaymentWithCategory>> = motDatabase
        .paymentDao()
        .getAllPaymentsWithCategoryOrderDateDescFlow()

    fun getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(categoryEntityId: Int): Flow<List<PaymentWithCategory>> = motDatabase
        .paymentDao()
        .getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(categoryEntityId)

    /**
     * Get payments WITHOUT end date used on Payments list screen to listen for the updates when new payment is added.
     */
    fun getPaymentsWithCategoryByDateRangeFlow(startTime: Long): Flow<List<PaymentWithCategory>> = motDatabase
        .paymentDao()
        .getPaymentsWithCategoryByDateRangeFlow(startTime)

    /**
     * Get payments WITH end date used to get payments in a particular time period.
     */
    fun getPaymentsWithCategoryByDateRangeFlow(startTime: Long, endTime: Long): Flow<List<PaymentWithCategory>> = motDatabase
        .paymentDao()
        .getPaymentsWithCategoryByDateRangeFlow(startTime, endTime)

    fun getAllPaymentsWithoutCategory(): Flow<List<PaymentWithCategory>> = motDatabase
        .paymentDao()
        .getAllPaymentsWithoutCategory()

    // ADD
    suspend fun addPayment(paymentEntity: PaymentEntity) = motDatabase
        .paymentDao()
        .insertPayment(paymentEntity)

    // EDIT
    suspend fun updatePayment(paymentEntity: PaymentEntity) = motDatabase
        .paymentDao()
        .updatePayment(paymentEntity)

    // DELETE
    suspend fun deletePayment(paymentEntity: PaymentEntity) = motDatabase
        .paymentDao()
        .deletePayment(paymentEntity)

    suspend fun deletePayments(paymentEntityList: List<PaymentEntity>) = motDatabase
        .paymentDao()
        .deletePayments(paymentEntityList)

}
