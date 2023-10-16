package dev.nelson.mot.main.domain.usecase.payment

import dev.nelson.mot.main.data.mapers.toPaymentEntity
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.usecase.base.UseCaseSuspend
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
    data object Add : ModifyPaymentAction()
    data object Edit : ModifyPaymentAction()
    data object Delete : ModifyPaymentAction()
}

data class ModifyPaymentParams(val payment: Payment, val action: ModifyPaymentAction)
