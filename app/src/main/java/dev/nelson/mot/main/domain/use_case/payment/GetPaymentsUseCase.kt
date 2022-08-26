package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPaymentsUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    fun execute(withCategory: Boolean = false, desOrder: Boolean = false): Flow<List<Payment>> {
        return paymentRepository.getAllPaymentsWithCategoryOrderDateDescFlow()
            .map { it.toPaymentList() }
    }
}