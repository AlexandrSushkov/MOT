package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.date_and_time.FormatTimeUseCase
import dev.nelson.mot.main.util.UUIDUtils
import dev.nelson.mot.main.util.constant.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetPaymentListNoFixedDateRange @Inject constructor(
    private val paymentRepository: PaymentRepositoryImpl,
    private val formatTimeUseCase: FormatTimeUseCase
) {

    /**
     * @param startTime time in epoc milliseconds.
     * @param category to find payments for. if null, ignore category, find any payment.
     * @param order sorting order for. Sorting field is [Payment.dateInMills].
     * @param onlyPaymentsWithoutCategory if true, only payments without category will be returned.
     */
    fun execute(
        startTime: Long = 0,
        category: Category? = null,
        order: SortingOrder = SortingOrder.Ascending,
        onlyPaymentsWithoutCategory: Boolean = false
    ): Flow<List<PaymentListItemModel>> {
        return paymentRepository.getPaymentsWithCategoryByCategoryIdNoFixedDateRange(
            startTime,
            category?.id,
            order is SortingOrder.Ascending,
        ).map { payments ->
            payments.filter { payment ->
                if (onlyPaymentsWithoutCategory) {
                    payment.categoryEntity == null
                } else {
                    true
                }
            }
        }.map { it.toPaymentList() }
            .map { it.formatDate(dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DAY_SHORT_MONTH_YEAR_DATE_PATTERN)) }
            .map { it.toPaymentListItemModelNew(category == null) }
    }

    /**
     *  Transform epoch mills into string date according to the system time zone
     */
    private fun List<Payment>.formatDate(
        timeZone: TimeZone? = null, dateTimeFormatter: DateTimeFormatter? = null
    ): List<Payment> {
        return this.map { payment ->
            payment.dateInMills.let { mills ->
                payment.copyWith(
                    dateText = formatTimeUseCase.execute(
                        mills, timeZone, dateTimeFormatter
                    )
                )
            }
        }
    }

    /**
     * Transform [Payment] to [PaymentListItemModel.PaymentItemModel] and divider items with date [PaymentListItemModel.Header]
     */
    private fun List<Payment>.toPaymentListItemModelNew(showCategory: Boolean): List<PaymentListItemModel> {
        return mutableListOf<PaymentListItemModel>().apply {
            this@toPaymentListItemModelNew.groupBy { payment -> payment.dateString }
                .forEach { (date, payments) ->
                    add(PaymentListItemModel.Header(date.orEmpty(), UUIDUtils.randomKey))
                    addAll(payments.toPaymentItemModelList(showCategory))
                }
            // TODO: add footer
        }
    }

    /**
     *Transform list of [Payment] to list of [PaymentListItemModel.PaymentItemModel]
     */
    private fun List<Payment>.toPaymentItemModelList(showCategory: Boolean): List<PaymentListItemModel> {
        return this.map { it.toPaymentListItem(showCategory) }
    }

    /**
     *Transform [Payment] to [PaymentListItemModel.PaymentItemModel]
     */
    private fun Payment.toPaymentListItem(showCategory: Boolean): PaymentListItemModel {
        return PaymentListItemModel.PaymentItemModel(this, showCategory, UUIDUtils.randomKey)
    }
}
