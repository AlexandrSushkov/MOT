package dev.nelson.mot.main.domain.usecase.statistic

import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.usecase.base.UseCaseFlow
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByCategoryModel
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByMonthModel
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.UUIDUtils
import dev.nelson.mot.main.util.extention.formatMillsToDateText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetStatisticByMonthUseCase @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl
) : UseCaseFlow<Nothing?, List<StatisticByMonthModel>> {

    private val calendar = Calendar.getInstance()

    override fun execute(params: Nothing?): Flow<List<StatisticByMonthModel>> {
        val payments = paymentRepository.getPaymentsWithCategoryByCategoryIdNoFixedDateRange(0)
        // sort payment by year
        return payments.map {
            it.groupBy { paymentWithCategory ->
                calendar.timeInMillis = paymentWithCategory.paymentEntity.dateInMilliseconds ?: 0
                calendar.get(Calendar.YEAR)
            }
        }.map { yearToPaymentsMap ->
            yearToPaymentsMap.map { yearPaymentsEntity ->
                yearPaymentsEntity.value.groupBy { paymentWithCategory ->
                    calendar.timeInMillis =
                        paymentWithCategory.paymentEntity.dateInMilliseconds ?: 0
                    calendar.get(Calendar.MONTH)
                }.map { monthPaymentsEntity ->
                    val sumTotal = monthPaymentsEntity.value.sumOf { it.paymentEntity.cost }
                    val categoryToPaymentListMap =
                        monthPaymentsEntity.value.groupBy { it.categoryEntity }
                    val statByCategoryList =
                        categoryToPaymentListMap.map { categoryPaymentsEntity ->
                            val sumOfPaymentsInCategory = categoryPaymentsEntity.value.sumOf { it.paymentEntity.cost }
                            val percentage = (sumOfPaymentsInCategory.toDouble() / sumTotal.toDouble()) * 100.0
                            StatisticByCategoryModel(
                                key = UUIDUtils.randomKey,
                                category = categoryPaymentsEntity.key?.toCategory(),
                                sumOfPayments = sumOfPaymentsInCategory,
                                percentage = percentage,
                                payments = categoryPaymentsEntity.value.toPaymentList()
                            )
                        }
                            .sortedByDescending { it.sumOfPayments } // sort categories by sum of payments, descending order
                    val sumOfCategories = statByCategoryList.sumOf { it.sumOfPayments }
                    val monthText = categoryToPaymentListMap.entries
                        .first()
                        .value
                        .first()
                        .paymentEntity
                        .dateInMilliseconds
                        ?.formatMillsToDateText() ?: StringUtils.EMPTY

                    StatisticByMonthModel(
                        key = UUIDUtils.randomKey,
                        monthText = monthText,
                        month = monthPaymentsEntity.key,
                        year = yearPaymentsEntity.key,
                        sumOfCategories = sumOfCategories,
                        categoriesModelList = statByCategoryList
                    )
                }
            }.flatten()
        }
    }
}
