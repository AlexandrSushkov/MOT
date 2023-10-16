package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import javax.inject.Inject

/**
 * Return list of [Payment] based on input parameters.
 *
 */
@Deprecated("Not implemented")
class GetPaymentListUseCase @Inject constructor(private val paymentRepository: PaymentRepositoryImpl) {

    // get payment divided by day
    // ordered
    // in time range
    // with category
}
