package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPayment
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    fun execute(paymentId: Int): Flow<Payment> = paymentRepository.getPayment(paymentId)
        .map { it.toPayment() }

}
