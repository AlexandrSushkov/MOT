package dev.nelson.mot.main.domain.use_case.statistic

import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.base.UseCaseFlow
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByCategoryModel
import dev.nelson.mot.main.util.UUIDUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetStatisticForCurrentMonthUseCase @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl
) : UseCaseFlow<Long, List<StatisticByCategoryModel>> {

    /**
     * params: start of current month time in millis
     */
    override fun execute(params: Long): Flow<List<StatisticByCategoryModel>> {
        return paymentRepository.getPaymentsWithCategoryByCategoryIdNoFixedDateRange(startTime = params)
            .map { payments ->
                payments.groupBy { paymentWithCategory ->
                    paymentWithCategory.categoryEntity
                }.map { categoryPaymentsEntity ->
                    val sumOfPaymentsInCategory =
                        categoryPaymentsEntity.value.sumOf { it.paymentEntity.cost }
                    StatisticByCategoryModel(
                        key = UUIDUtils.randomKey,
                        category = categoryPaymentsEntity.key?.toCategory(),
                        sumOfPayments = sumOfPaymentsInCategory,
                        payments = categoryPaymentsEntity.value.toPaymentList()
                    )
                }.sortedByDescending { it.sumOfPayments } // sort categories by sum of payments, descending order
            }
    }
}
