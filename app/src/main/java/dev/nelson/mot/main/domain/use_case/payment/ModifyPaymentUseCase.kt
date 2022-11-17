package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentEntity
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepository
import javax.inject.Inject

class ModifyPaymentUseCase@Inject constructor(private val paymentRepository: PaymentRepository) {

    suspend fun execute(payment: Payment, action: ModifyPaymentAction) {
        val paymentEntity = payment.toPaymentEntity()
        when(action){
            ModifyPaymentAction.Add -> paymentRepository.addPayment(paymentEntity)
            ModifyPaymentAction.Edit -> paymentRepository.updatePayment(paymentEntity)
            ModifyPaymentAction.Delete -> paymentRepository.deletePayment(paymentEntity)
        }
    }
}

sealed class ModifyPaymentAction{
    object Add: ModifyPaymentAction()
    object Edit: ModifyPaymentAction()
    object Delete: ModifyPaymentAction()
}
