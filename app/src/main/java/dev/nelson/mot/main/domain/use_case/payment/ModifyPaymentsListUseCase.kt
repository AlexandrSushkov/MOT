package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentEntityList
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import javax.inject.Inject

/**
 * The same as [ModifyPaymentUseCase], but works with list.
 */
class ModifyPaymentsListUseCase @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl
) : UseCaseSuspend<ModifyPaymentsListParams, Unit> {

    override suspend fun execute(params: ModifyPaymentsListParams) {
        val paymentsEntityList = params.payments.toPaymentEntityList()
        when (params.action) {
            ModifyPaymentsListAction.Edit -> paymentRepository.updatePayments(paymentsEntityList)
            ModifyPaymentsListAction.Delete -> paymentRepository.deletePayments(paymentsEntityList)
        }
    }
}

sealed class ModifyPaymentsListAction {
    object Edit : ModifyPaymentsListAction()
    object Delete : ModifyPaymentsListAction()
}

data class ModifyPaymentsListParams(
    val payments: List<Payment>,
    val action: ModifyPaymentsListAction
)
