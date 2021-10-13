package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepository @Inject constructor(private val motDatabase: MotDatabase) {

    fun getAllPayments(): Flowable<List<PaymentEntity>> =
        motDatabase.paymentDao()
            .getAllPaymentsRx()

    fun getAllPaymentsWithCategory(): Flowable<List<PaymentWithCategory>> =
        motDatabase.paymentDao()
            .getAllPaymentsWithCategoryRx()

    fun addPayments(paymentEntity: PaymentEntity): Single<List<Long>> =
        motDatabase.paymentDao()
            .addPayments(listOf(paymentEntity))


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

    suspend fun getAllPaymentsWithCategoryOrderedByIdDescCor(): List<PaymentWithCategory> =
        motDatabase.paymentDao()
            .getAllPaymentsWithCategoryOrderedByIdDescCor()

    suspend fun getAllPaymentsWithCategoryCor(): List<PaymentWithCategory> =
        motDatabase.paymentDao()
            .getAllPaymentsWithCategoryCor()

    fun getAllPaymentsWithCategoryOrderDateDescFlow(): Flow<List<PaymentWithCategory>> =
        motDatabase.paymentDao()
            .getAllPaymentsWithCategoryOrderDateDescFlow()
}
