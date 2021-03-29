package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.toPaymentList
import dev.nelson.mot.main.data.repository.PaymentRepository
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    fun getAllPayments(): Flowable<List<Payment>> = paymentRepository.getAllPaymentsWithCategory()
        .map { it.toPaymentList() }
        .subscribeOn(Schedulers.io())

    suspend fun getAllPaymentsCor(): List<Payment> = paymentRepository.getAllPaymentsWithCategoryCor()
        .toPaymentList()
}
