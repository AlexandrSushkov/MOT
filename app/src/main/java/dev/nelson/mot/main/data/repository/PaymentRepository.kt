package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepository @Inject constructor(private val motDatabase: MotDatabase) {

    suspend fun addPaymentCor(paymentEntity: PaymentEntity) =
        motDatabase.paymentDao()
            .insertPayment(paymentEntity)

    suspend fun updatePaymentCor(paymentEntity: PaymentEntity) =
        motDatabase.paymentDao()
            .updatePayment(paymentEntity)

    suspend fun deletePaymentCor(paymentEntity: PaymentEntity) =
        motDatabase.paymentDao()
            .deletePayment(paymentEntity)

    suspend fun getAllPaymentsWithCategoryOrderDateDescCor(): List<PaymentWithCategory> =
        motDatabase.paymentDao()
            .getAllPaymentsWithCategoryOrderDateDescCor()

    suspend fun getAllPaymentsWithCategoryCor(): List<PaymentWithCategory> =
        motDatabase.paymentDao()
            .getAllPaymentsWithCategoryCor()

    fun getAllPaymentsWithCategoryOrderDateDescFlow(): Flow<List<PaymentWithCategory>> =
        motDatabase.paymentDao()
            .getAllPaymentsWithCategoryOrderDateDescFlow()

    fun getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(categoryEntityId: Int): Flow<List<PaymentWithCategory>> =
        motDatabase.paymentDao()
            .getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(categoryEntityId)

    fun getAllPaymentsWithoutCategory(): Flow<List<PaymentWithCategory>> =
        motDatabase.paymentDao()
            .getAllPaymentsWithoutCategory()
}
