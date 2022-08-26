package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepository @Inject constructor(private val motDatabase: MotDatabase) {

    fun getPayment(paymentId: Int): Flow<PaymentWithCategory> =
        motDatabase.paymentDao()
            .getPaymentById(paymentId)

    suspend fun addPayment(paymentEntity: PaymentEntity) =
        motDatabase.paymentDao()
            .insertPayment(paymentEntity)

    suspend fun updatePayment(paymentEntity: PaymentEntity) =
        motDatabase.paymentDao()
            .updatePayment(paymentEntity)

    suspend fun deletePayment(paymentEntity: PaymentEntity) =
        motDatabase.paymentDao()
            .deletePayment(paymentEntity)

    suspend fun getAllPaymentsWithCategoryOrderDateDesc(): List<PaymentWithCategory> =
        motDatabase.paymentDao()
            .getAllPaymentsWithCategoryOrderDateDescCor()

    suspend fun getAllPaymentsWithCategory(): List<PaymentWithCategory> =
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
