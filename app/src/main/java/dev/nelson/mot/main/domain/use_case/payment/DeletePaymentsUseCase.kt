package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentEntity
import dev.nelson.mot.main.data.mapers.toPaymentEntityList
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepository
import javax.inject.Inject

class DeletePaymentsUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    suspend fun execute(payments: List<Payment>) {
        val paymentEntityList = payments.toPaymentEntityList()
        paymentRepository.deletePayments(paymentEntityList)
    }
}