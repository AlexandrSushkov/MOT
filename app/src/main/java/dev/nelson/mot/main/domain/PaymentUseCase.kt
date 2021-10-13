package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.toPaymentEntity
import dev.nelson.mot.main.data.model.toPaymentList
import dev.nelson.mot.main.data.repository.PaymentRepository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    fun getAllPayments(): Flowable<List<Payment>> = paymentRepository.getAllPaymentsWithCategory()
        .map { it.toPaymentList() }
        .subscribeOn(Schedulers.io())



    fun addPayments(payment: Payment): Single<List<Long>> =
        Single.just(payment)
            .map { it.toPaymentEntity() }
            .flatMap { paymentRepository.addPayments(it) }
            .subscribeOn(Schedulers.io())


    suspend fun addPayment(payment: Payment) {
        val paymentEntity = payment.toPaymentEntity()
        paymentRepository.addPaymentCor(paymentEntity)
    }

    suspend fun editPayment(payment: Payment) {
        val paymentEntity = payment.toPaymentEntity()
        paymentRepository.updatePaymentCor(paymentEntity)
    }

    suspend fun deletePayment(payment: Payment){
        val paymentEntity = payment.toPaymentEntity()
        paymentRepository.deletePaymentCor(paymentEntity)
    }


    suspend fun getAllPaymentsOrderDateDescCor() = paymentRepository.getAllPaymentsWithCategoryOrderDateDescCor()

    fun getAllPaymentsWithCategoryOrderDateDescFlow() = paymentRepository.getAllPaymentsWithCategoryOrderDateDescFlow()
        .map { it.toPaymentList() }


    suspend fun getAllPaymentsWithCategoryOrderedByIdDescCor() = paymentRepository.getAllPaymentsWithCategoryOrderedByIdDescCor()

    suspend fun getAllPaymentsCor(): List<Payment> = paymentRepository.getAllPaymentsWithCategoryCor()
        .toPaymentList()
}
