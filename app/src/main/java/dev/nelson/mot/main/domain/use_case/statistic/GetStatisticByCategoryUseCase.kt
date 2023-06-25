package dev.nelson.mot.main.domain.use_case.statistic

import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.base.UseCaseFlow
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.UUIDUtils
import dev.nelson.mot.main.util.extention.convertMillisecondsToDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetStatisticByCategoryUseCase @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl,
) : UseCaseFlow<Nothing?, List<StatisticByCategoryPerMonthModel>> {

    private val calendar = Calendar.getInstance()

    override fun execute(params: Nothing?): Flow<List<StatisticByCategoryPerMonthModel>> {
        return paymentRepository.getPaymentsWithCategoryByCategoryIdNoFixedDateRange(
            startTime = 0,
        ).map { paymentsWithCategoryList ->
            paymentsWithCategoryList.groupBy { paymentWithCategory ->
                paymentWithCategory.categoryEntity
            }.map { categoryToPaymentsListEntry ->
                val cat = categoryToPaymentsListEntry.key
                val paymentsForCategory = categoryToPaymentsListEntry.value
                val resultForCategoryByMonth =
                    paymentsForCategory.groupBy { listOfPaymentsForCategory ->
                        calendar.timeInMillis =
                            listOfPaymentsForCategory.paymentEntity.dateInMilliseconds ?: 0
                        calendar.get(Calendar.YEAR)
                    }.map { yearToPaymentMap ->
                        val year = yearToPaymentMap.key
                        val paymentsForYear = yearToPaymentMap.value
                        paymentsForYear.groupBy { paymentWithCategory ->
                            calendar.timeInMillis =
                                paymentWithCategory.paymentEntity.dateInMilliseconds ?: 0
                            calendar.get(Calendar.MONTH)
                        }.map { monthToPaymentMap ->
                            val paymentsForMonth = monthToPaymentMap.value
                            val sumOfPaymentsForThisMonth =
                                paymentsForMonth.sumOf { paymentWithCategory -> paymentWithCategory.paymentEntity.cost }
                            val monthText = paymentsForMonth.first()
                                .paymentEntity
                                .dateInMilliseconds
                                ?.convertMillisecondsToDate() ?: StringUtils.EMPTY
                            val month = Month(
                                monthText = monthText,
                                monthToPaymentMap.key,
                                year
                            )
                            val model = StatisticForMonthForCategoryModel(
                                sumOfPaymentsForThisMonth = sumOfPaymentsForThisMonth,
                                payments = monthToPaymentMap.value.toPaymentList()
                            )
                            month to model
                        }
                    }.flatten().toMap()

                val totalPrice =
                    resultForCategoryByMonth.values.sumOf { it.sumOfPaymentsForThisMonth }
                StatisticByCategoryPerMonthModel(
                    key = UUIDUtils.randomKey,
                    totalPrice = totalPrice,
                    category = cat?.toCategory() ?: Category("No category"),
                    paymentToMonth = resultForCategoryByMonth
                )
            }
        }
    }
}

/**
 * holds:
 * - a category.
 * - map of month with payments for this month for a particular category
 */
data class StatisticByCategoryPerMonthModel(
    val key: String,
    val category: Category? = null,
    val totalPrice: Int = 0,
    val paymentToMonth: Map<Month, StatisticForMonthForCategoryModel>
)

data class Month(
    val monthText: String = StringUtils.EMPTY,
    val month: Int = 0,
    val year: Int = 0,
)

/**
 *
 */
data class StatisticForMonthForCategoryModel(
    val sumOfPaymentsForThisMonth: Int = 0,
    val payments: List<Payment>,
)