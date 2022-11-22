package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.model.payment.PaymentDao
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import dev.nelson.mot.main.util.Order
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

//    fun getAllPaymentsWithCategoryOrderByDateDesc(): Flow<List<PaymentWithCategory>> = paymentDao.getAllPaymentsWithCategoryOrderDateDesc()

    /**
     * Get payments WITHOUT end date used on Payments list screen to listen for the updates when new payment is added.
     */
    fun getPaymentsWithCategoryByDateRangeOrdered(startTime: Long, order: Order): Flow<List<PaymentWithCategory>> {
        return when (order) {
            Order.Ascending -> paymentDao.getPaymentsWithCategoryByDateRangeOrderedAscending(startTime)
            Order.Descending -> paymentDao.getPaymentsWithCategoryByDateRangeOrderedDescending(startTime)
        }
    }

    /**
     * Get payments WITH end date used to get payments in a particular time period.
     */
    fun getPaymentsWithCategoryByDateRangeOrdered(startTime: Long, endTime: Long, order: Order): Flow<List<PaymentWithCategory>> {
        return when (order) {
            Order.Ascending -> paymentDao.getPaymentsWithCategoryByDateRangeOrderedAscending(startTime, endTime)
            Order.Descending -> paymentDao.getPaymentsWithCategoryByDateRangeOrderedDescending(startTime, endTime)
        }
    }

    // ADD
    suspend fun addPayment(paymentEntity: PaymentEntity) = paymentDao.addPayment(paymentEntity)

    // EDIT
    suspend fun updatePayment(paymentEntity: PaymentEntity) = paymentDao.updatePayment(paymentEntity)

    // DELETE
    suspend fun deletePayment(paymentEntity: PaymentEntity) = paymentDao.deletePayment(paymentEntity)

    suspend fun deletePayments(paymentEntityList: List<PaymentEntity>) = paymentDao.deletePayments(paymentEntityList)
}
