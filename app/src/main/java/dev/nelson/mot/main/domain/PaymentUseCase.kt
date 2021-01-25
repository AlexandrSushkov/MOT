package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.repository.PaymentRepository
import dev.nelson.mot.main.data.room.model.payment.Payment
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    fun getAllPayments(): Flowable<List<Payment>> = paymentRepository.getAllPayments()
        .subscribeOn(Schedulers.io())
}
