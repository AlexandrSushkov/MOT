package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.MotPaymentListItemModel
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.date_and_time.FormatTimeUseCase
import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.util.UUIDUtils
import dev.nelson.mot.main.util.constant.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * By default it returns list of [Payment] for current month sorted by the time, starting from the latest added [Payment].
 */
class GetPaymentListByFixedDateRangeUseCase @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl,
    private val formatTimeUseCase: FormatTimeUseCase
) {

    /**
     * @param startTime time in epoc milliseconds.
     * @param endTime time in epoc milliseconds.
     * @param category to find payments for. if null, ignore category, find any payment.
     */
    fun execute(
        startTime: Long = 0,
        endTime: Long? = null,
        category: Category? = null,
        order: SortingOrder = SortingOrder.Ascending
    ): Flow<List<MotPaymentListItemModel>> {
        val paymentsWithCategoryList = endTime?.let {
            paymentRepository.getPaymentsWithCategoryByFixedDateRange(
                startTime,
                it,
                order is SortingOrder.Ascending,
            )
        }
            ?: paymentRepository.getPaymentsWithCategoryByCategoryIdNoFixedDateRange(
                startTime,
                category?.id,
                order is SortingOrder.Ascending
            )

        return paymentsWithCategoryList
            .map { it.toPaymentList() }
            .map { it.addDateText(dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DAY_SHORT_MONTH_YEAR_DATE_PATTERN)) }
            .map { it.toPaymentListItemModelNew(category == null) }
    }

    /**
     * Transform epoch mills into string date according to the system time zone
     */
    private fun List<Payment>.addDateText(
        timeZone: TimeZone? = null,
        dateTimeFormatter: DateTimeFormatter? = null
    ): List<Payment> {
        return this.map { payment ->
            payment.dateInMills.let { mills ->
                payment.copyWith(
                    // TODO: can be replaced with Long.convertMillisecondsToDate
                    dateText = formatTimeUseCase.execute(
                        mills,
                        timeZone,
                        dateTimeFormatter
                    )
                )
            }
        }
    }

    /**
     * Transform [Payment] to [MotPaymentListItemModel.Item] and divider items with date [MotPaymentListItemModel.Header]
     */
    private fun List<Payment>.toPaymentListItemModelNew(showCategory: Boolean): List<MotPaymentListItemModel> {
        return mutableListOf<MotPaymentListItemModel>().apply {
            this@toPaymentListItemModelNew.groupBy { payment -> payment.dateString }
                .forEach { (date, payments) ->
                    add(MotPaymentListItemModel.Header(date.orEmpty(), UUIDUtils.randomKey))
                    addAll(payments.toPaymentItemModelList(showCategory))
                }
            if (this.isNotEmpty()) {
                add(MotPaymentListItemModel.Footer(UUIDUtils.randomKey))
            }
        }
    }

    /**
     *Transform list of [Payment] to list of [MotPaymentListItemModel.Item]
     */
    private fun List<Payment>.toPaymentItemModelList(showCategory: Boolean): List<MotPaymentListItemModel> {
        return this.map { it.toPaymentListItem(showCategory) }
    }

    /**
     *Transform [Payment] to [MotPaymentListItemModel.Item]
     */
    private fun Payment.toPaymentListItem(showCategory: Boolean): MotPaymentListItemModel {
        return MotPaymentListItemModel.Item(this, showCategory, UUIDUtils.randomKey)
    }

}
