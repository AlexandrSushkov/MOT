package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.model.payment.PaymentDao
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepository @Inject constructor(private val paymentDao: PaymentDao) {

    //GET
    /**
     * Get payment
     *
     * @param paymentId id of the payment
     * @return [PaymentWithCategory]
     */
    fun getPayment(paymentId: Int): Flow<PaymentWithCategory> = paymentDao.getPaymentById(paymentId)

//    suspend fun getAllPaymentsWithCategoryOrderDateDesc(): List<PaymentWithCategory> = paymentDao
//        .getAllPaymentsWithCategoryOrderDateDescCor()

    suspend fun getAllPaymentsWithCategory(): List<PaymentWithCategory> = paymentDao.getAllPaymentsWithCategoryCor()

    fun getAllPaymentsWithCategoryOrderDateDescFlow(): Flow<List<PaymentWithCategory>> = paymentDao.getAllPaymentsWithCategoryOrderDateDescFlow()

    fun getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(categoryEntityId: Int): Flow<List<PaymentWithCategory>> = paymentDao
        .getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(categoryEntityId)

    /**
     * Get payments WITHOUT end date used on Payments list screen to listen for the updates when new payment is added.
     */
    fun getPaymentsWithCategoryByDateRangeFlow(startTime: Long): Flow<List<PaymentWithCategory>> = paymentDao.getPaymentsWithCategoryByDateRangeFlow(startTime)

    /**
     * Get payments WITH end date used to get payments in a particular time period.
     */
    fun getPaymentsWithCategoryByDateRangeFlow(startTime: Long, endTime: Long): Flow<List<PaymentWithCategory>> = paymentDao
        .getPaymentsWithCategoryByDateRangeFlow(startTime, endTime)

    fun getAllPaymentsWithoutCategory(): Flow<List<PaymentWithCategory>> = paymentDao.getAllPaymentsWithoutCategory()

    // ADD
    suspend fun addPayment(paymentEntity: PaymentEntity) = paymentDao.insertPayment(paymentEntity)

    // EDIT
    suspend fun updatePayment(paymentEntity: PaymentEntity) = paymentDao.updatePayment(paymentEntity)

    // DELETE
    suspend fun deletePayment(paymentEntity: PaymentEntity) = paymentDao.deletePayment(paymentEntity)

    suspend fun deletePayments(paymentEntityList: List<PaymentEntity>) = paymentDao.deletePayments(paymentEntityList)
}
