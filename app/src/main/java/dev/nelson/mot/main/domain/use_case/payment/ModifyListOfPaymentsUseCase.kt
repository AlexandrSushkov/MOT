package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentEntityList
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepository
import javax.inject.Inject

/**
 * The same as [ModifyPaymentUseCase] but works with list
 */
class ModifyListOfPaymentsUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    suspend fun execute(payments: List<Payment>, action: ModifyListOfPaymentsAction) {
        val paymentsEntityList = payments.toPaymentEntityList()
        when (action) {
            ModifyListOfPaymentsAction.Edit -> paymentRepository.updatePayments(paymentsEntityList)
            ModifyListOfPaymentsAction.Delete -> paymentRepository.deletePayments(paymentsEntityList)
        }
    }
}

sealed class ModifyListOfPaymentsAction {
    object Edit : ModifyListOfPaymentsAction()
    object Delete : ModifyListOfPaymentsAction()
}
