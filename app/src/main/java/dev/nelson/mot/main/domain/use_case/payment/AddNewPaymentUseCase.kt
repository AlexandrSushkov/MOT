package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentEntity
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepository
import javax.inject.Inject

// TODO: rewrite to ModifyPaymentUseCase with mode(add, edit, delete)
class AddNewPaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    suspend fun execute(payment: Payment) {
        val paymentEntity = payment.toPaymentEntity()
        paymentRepository.addPayment(paymentEntity)
    }
}