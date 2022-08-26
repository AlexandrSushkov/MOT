package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    fun getAllPaymentsWithCategoryOrderDateDescFlow(): Flow<List<Payment>> {
        return paymentRepository.getAllPaymentsWithCategoryOrderDateDescFlow()
            .map { it.toPaymentList() }
    }

    fun getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(categoryId: Int): Flow<List<Payment>> {
        return paymentRepository.getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(categoryId)
            .map { it.toPaymentList() }
    }

    fun getAllPaymentsWithoutCategory(): Flow<List<Payment>> {
        return paymentRepository.getAllPaymentsWithoutCategory()
            .map { it.toPaymentList() }
    }

    suspend fun getAllPaymentsCor(): List<Payment> = paymentRepository.getAllPaymentsWithCategory()
        .toPaymentList()
}
