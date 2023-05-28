package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPayment
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.base.UseCaseFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl
) : UseCaseFlow<Int, Payment> {

    /**
     * @param params payment ID
     * @return [Payment]
     */
    override fun execute(params: Int): Flow<Payment> = paymentRepository.getPayment(params)
        .map { it.toPayment() }
}
