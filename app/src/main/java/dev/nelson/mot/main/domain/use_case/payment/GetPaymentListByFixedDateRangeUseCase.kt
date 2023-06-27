package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.date_and_time.FormatTimeUseCase
import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.util.UUIDUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * By default it returns list of [Payment] for current month sorted by the time, starting from the latest added [Payment].
 */
class GetPaymentListByFixedDateRange @Inject constructor(
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
    ): Flow<List<PaymentListItemModel>> {
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
            .map { it.formatDate(dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")) }
            .map { it.toPaymentListItemModelNew(category == null) }
    }

    /**
     * transform epoch mills into string date according to the system time zone
     */
    private fun List<Payment>.formatDate(
        timeZone: TimeZone? = null,
        dateTimeFormatter: DateTimeFormatter? = null
    ): List<Payment> {
        return this.map { payment ->
            payment.dateInMills?.let { mills ->
                payment.copyWith(
                    dateText = formatTimeUseCase.execute(
                        mills,
                        timeZone,
                        dateTimeFormatter
                    )
                )
            } ?: payment
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
