package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentEntity
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import javax.inject.Inject

/**
 * Add, edit or delete a [Payment].
 * @see [ModifyPaymentsListUseCase]
 */
class ModifyPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl
) : UseCaseSuspend<ModifyPaymentParams, Unit> {

    override suspend fun execute(params: ModifyPaymentParams) {
        val paymentEntity = params.payment.toPaymentEntity()
        when (params.action) {
            ModifyPaymentAction.Add -> paymentRepository.addPayment(paymentEntity)
            ModifyPaymentAction.Edit -> paymentRepository.updatePayment(paymentEntity)
            ModifyPaymentAction.Delete -> paymentRepository.deletePayment(paymentEntity)
        }
    }
}

sealed class ModifyPaymentAction {
    object Add : ModifyPaymentAction()
    object Edit : ModifyPaymentAction()
    object Delete : ModifyPaymentAction()
}

data class ModifyPaymentParams(val payment: Payment, val action: ModifyPaymentAction)
