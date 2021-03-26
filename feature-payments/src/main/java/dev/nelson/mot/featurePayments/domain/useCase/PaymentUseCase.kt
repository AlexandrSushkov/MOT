package dev.nelson.mot.featurePayments.domain.useCase

import dev.nelson.mot.featurePayments.data.repository.PaymentRepository
import dev.nelson.mot.featurePayments.data.repository.PaymentRepositoryImpl

class PaymentUseCase {
    private val paymentRepository: PaymentRepository = PaymentRepositoryImpl()

    fun getPayment() :String = paymentRepository.getPayment()
}