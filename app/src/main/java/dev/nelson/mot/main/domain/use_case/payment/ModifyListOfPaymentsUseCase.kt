package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentEntityList
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.UseCaseSuspend
import javax.inject.Inject

/**
 * The same as [ModifyPaymentUseCase] but works with list
 */
class ModifyListOfPaymentsUseCase @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl
) : UseCaseSuspend<ModifyListOfPaymentsParams, Unit> {

    override suspend fun execute(params: ModifyListOfPaymentsParams) {
        val paymentsEntityList = params.payments.toPaymentEntityList()
        when (params.action) {
            ModifyListOfPaymentsAction.Edit -> paymentRepository.updatePayments(paymentsEntityList)
            ModifyListOfPaymentsAction.Delete -> paymentRepository.deletePayments(paymentsEntityList)
        }
    }
}

data class ModifyListOfPaymentsParams(val payments: List<Payment>, val action: ModifyListOfPaymentsAction)

sealed class ModifyListOfPaymentsAction {
    object Edit : ModifyListOfPaymentsAction()
    object Delete : ModifyListOfPaymentsAction()
}
