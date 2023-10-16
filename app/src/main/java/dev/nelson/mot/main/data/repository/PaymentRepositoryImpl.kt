package dev.nelson.mot.main.data.repository

import dev.nelson.mot.db.model.payment.PaymentDao
import dev.nelson.mot.db.model.payment.PaymentEntity
import dev.nelson.mot.db.model.paymentjoin.PaymentWithCategoryEntity
import dev.nelson.mot.main.data.repository.base.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentDao
) : PaymentRepository {

    // GET
    /**
     * Get payment
     *
     * @param paymentId id of the payment
     * @return [PaymentWithCategoryEntity]
     */
    override fun getPayment(paymentId: Int): Flow<PaymentWithCategoryEntity> =
        paymentDao.getPaymentById(paymentId)

    override suspend fun getAllPayments(): List<PaymentEntity> {
        return paymentDao.getAllPayments()
    }

    override fun getAllPaymentsWithoutCategory(): Flow<List<PaymentWithCategoryEntity>> {
        return paymentDao.getAllPaymentsWithoutCategory()
    }

    /**
     * Get payments WITHOUT end date used on Payments list screen to listen for the updates when new payment is added.
     * @param isAsc true if ascending order is needed, false otherwise.
     */
    override fun getPaymentsWithCategoryByCategoryIdNoFixedDateRange(
        startTime: Long,
        categoryId: Int?,
        isAsc: Boolean
    ): Flow<List<PaymentWithCategoryEntity>> {
        return categoryId?.let {
            paymentDao.getPaymentsWithCategoryByCategoryIdNoFixedDateRange(
                startTime,
                it,
                isAsc
            )
        } ?: paymentDao.getPaymentsWithCategoryNoCategoryNoFixedDateRange(startTime, isAsc)
    }

    /**
     * Get payments WITH end date used to get payments in a particular time period.
     * @param isAsc true if ascending order is needed, false otherwise.
     */
    override fun getPaymentsWithCategoryByFixedDateRange(
        startTime: Long,
        endTime: Long,
        isAsc: Boolean
    ): Flow<List<PaymentWithCategoryEntity>> {
        return paymentDao.getPaymentsWithCategoryByFixedDateRange(
            startTime,
            endTime,
            isAsc
        )
    }

    // ADD
    override suspend fun addPayment(paymentEntity: PaymentEntity) =
        paymentDao.addPayment(paymentEntity)

    // EDIT
    override suspend fun updatePayment(paymentEntity: PaymentEntity) =
        paymentDao.updatePayment(paymentEntity)

    override suspend fun updatePayments(paymentEntityList: List<PaymentEntity>) =
        paymentDao.updatePayments(paymentEntityList)

    // DELETE
    override suspend fun deletePayment(paymentEntity: PaymentEntity) =
        paymentDao.deletePayment(paymentEntity)

    override suspend fun deletePayments(paymentEntityList: List<PaymentEntity>) =
        paymentDao.deletePayments(paymentEntityList)
}
