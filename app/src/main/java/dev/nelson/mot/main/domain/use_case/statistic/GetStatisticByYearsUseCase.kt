package dev.nelson.mot.main.domain.use_case.statistic

import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.base.UseCaseFlow
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByCategoryModel
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByYearModel
import dev.nelson.mot.main.util.UUIDUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetStatisticByYearsUseCase @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl,
) : UseCaseFlow<Nothing?, List<StatisticByYearModel>> {

    private val calendar = Calendar.getInstance()

    override fun execute(params: Nothing?): Flow<List<StatisticByYearModel>> {
        val payments = paymentRepository.getPaymentsWithCategoryByCategoryIdNoFixedDateRange(
            startTime = 0,
            categoryId = null,
        )
        // sort payment by year
        return payments.map {
            it.groupBy { paymentWithCategory ->
                calendar.timeInMillis = paymentWithCategory.paymentEntity.dateInMilliseconds ?: 0
                calendar.get(Calendar.YEAR)
            }
        }.map { yearPaymentsMap ->
            yearPaymentsMap.map { yearPaymentsEntity ->
                val categoryToPaymentListMap = yearPaymentsEntity.value.groupBy { it.categoryEntity }
                val statByCategoryList = categoryToPaymentListMap.map { categoryPaymentsEntity ->
                    val sumOfPaymentsInCategory =
                        categoryPaymentsEntity.value.sumOf { it.paymentEntity.cost }
                    StatisticByCategoryModel(
                        key = UUIDUtils.randomKey,
                        category = categoryPaymentsEntity.key?.toCategory(),
                        sumOfPayments = sumOfPaymentsInCategory,
                        payments = categoryPaymentsEntity.value.toPaymentList()
                    )
                }.sortedByDescending { it.sumOfPayments } // sort categories by sum of payments, descending order
                val sumOfCategories = statByCategoryList.sumOf { it.sumOfPayments }
                StatisticByYearModel(
                    key = UUIDUtils.randomKey,
                    year = yearPaymentsEntity.key,
                    sumOfCategories = sumOfCategories,
                    categoriesModelList = statByCategoryList
                )
            }
        }
    }
}
