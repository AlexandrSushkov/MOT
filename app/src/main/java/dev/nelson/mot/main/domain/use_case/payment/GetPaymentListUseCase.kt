package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Return list of [Payment] based on input parameters.
 *
 */
class GetPaymentListUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {

    // get payment divided by day
    // ordered
    // in time range
    // with category
}