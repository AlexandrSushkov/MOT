package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.repository.base.PaymentRepository
import dev.nelson.mot.main.data.room.model.payment.PaymentDao
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import dev.nelson.mot.main.util.SortingOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val paymentDao: PaymentDao):PaymentRepository {

    //GET
    /**
     * Get payment
     *
     * @param paymentId id of the payment
     * @return [PaymentWithCategory]
     */
    override fun getPayment(paymentId: Int): Flow<PaymentWithCategory> = paymentDao.getPaymentById(paymentId)

    /**
     * Get payments WITHOUT end date used on Payments list screen to listen for the updates when new payment is added.
     */
    override fun getPaymentsWithCategoryByDateRangeOrdered(startTime: Long, order: SortingOrder, categoryId: Int?): Flow<List<PaymentWithCategory>> {
        return when (order) {
            SortingOrder.Ascending -> {
                categoryId?.let {
                    paymentDao.getPaymentsWithCategoryByDateRangeAndCategoryOrderedAscending(startTime, it)
                } ?: paymentDao.getPaymentsWithCategoryByDateRangeOrderedAscending(startTime)

            }
            SortingOrder.Descending -> {
                categoryId?.let {
                    paymentDao.getPaymentsWithCategoryByDateRangeAndCategoryOrderedDescending(startTime, it)
                } ?: paymentDao.getPaymentsWithCategoryByDateRangeOrderedDescending(startTime)

            }
        }
    }

    /**
     * Get payments WITH end date used to get payments in a particular time period.
     */
    override fun getPaymentsWithCategoryByDateRangeOrdered(startTime: Long, endTime: Long, order: SortingOrder): Flow<List<PaymentWithCategory>> {
        return when (order) {
            SortingOrder.Ascending -> paymentDao.getPaymentsWithCategoryByDateRangeOrderedAscending(startTime, endTime)
            SortingOrder.Descending -> paymentDao.getPaymentsWithCategoryByDateRangeOrderedDescending(startTime, endTime)
        }
    }

    // ADD
    override suspend fun addPayment(paymentEntity: PaymentEntity) = paymentDao.addPayment(paymentEntity)

    // EDIT
    override suspend fun updatePayment(paymentEntity: PaymentEntity) = paymentDao.updatePayment(paymentEntity)

    override suspend fun updatePayments(paymentEntityList: List<PaymentEntity>) = paymentDao.updatePayments(paymentEntityList)

    // DELETE
    override suspend fun deletePayment(paymentEntity: PaymentEntity) = paymentDao.deletePayment(paymentEntity)

    override suspend fun deletePayments(paymentEntityList: List<PaymentEntity>) = paymentDao.deletePayments(paymentEntityList)
}
