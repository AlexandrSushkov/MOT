package dev.nelson.mot.main.domain.use_case.payment

import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.mapers.toPaymentList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.data.repository.PaymentRepository
import dev.nelson.mot.main.domain.use_case.date_and_time.FormatTimeUseCase
import dev.nelson.mot.main.util.SortingOrder
import dev.nelson.mot.main.util.UUIDUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * By default it returns list of [Payment] for current month sorted by the time, starting from the latest added [Payment].
 */
class GetPaymentListByDateRange @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val formatTimeUseCase: FormatTimeUseCase
) {

    fun execute(
        startTime: Long = 0,
        endTime: Long? = null,
        category: Category? = null,
        order: SortingOrder = SortingOrder.Ascending
    ): Flow<List<PaymentListItemModel>> {
        val paymentsWithCategoryList = endTime?.let { paymentRepository.getPaymentsWithCategoryByDateRangeOrdered(startTime, it, order) }
            ?: paymentRepository.getPaymentsWithCategoryByDateRangeOrdered(startTime, order, category?.id)

        return paymentsWithCategoryList
            .map { it.toPaymentList() }
            .map { it.formatDate() }
//            .map { it.toPaymentListItemModel() }
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
            payment.dateInMills?.let { mills -> payment.copyWith(date = formatTimeUseCase.execute(mills, timeZone, dateTimeFormatter)) } ?: payment
        }
    }

    /**
     * Transform list [Payment]. Add date divider item
     * @return list of [PaymentListItemModel]
     */
    @Deprecated(message = "replace with List<Payment>.toPaymentListItemModelNew()")
    private fun List<Payment>.toPaymentListItemModel(): List<PaymentListItemModel> {
        // represents the day when payment/s was added
       /*var dateCursor: String? = null
        val result = mutableListOf<PaymentListItemModel>()
        this.forEach { payment ->
            if (dateCursor == null) {
                dateCursor = payment.date
                result.add(PaymentListItemModel.Header(payment.date.orEmpty(), UUIDUtils.getRandomKey))
            }
            if (dateCursor == payment.date) {
                // same day
                result.add(PaymentListItemModel.PaymentItemModel(payment, UUIDUtils.getRandomKey))
            } else {
                // next day
                dateCursor = payment.date
                result.add(PaymentListItemModel.Header(payment.date.orEmpty(), UUIDUtils.getRandomKey))
                result.add(PaymentListItemModel.PaymentItemModel(payment, UUIDUtils.getRandomKey))
            }
        }
        // TODO: add footer
        return result.toList()*/
        return emptyList()

    }

    /**
     * Transform [Payment] to [PaymentListItemModel.PaymentItemModel] and divider items with date [PaymentListItemModel.Header]
     */
    private fun List<Payment>.toPaymentListItemModelNew(shotCategory: Boolean,): List<PaymentListItemModel> {
        return mutableListOf<PaymentListItemModel>().apply {
            this@toPaymentListItemModelNew.groupBy { payment -> payment.date }
                .forEach { (date, payments) ->
                    add(PaymentListItemModel.Header(date.orEmpty(), UUIDUtils.getRandomKey))
                    addAll(payments.toPaymentItemModelList(shotCategory))
                }
            // TODO: add footer
        }
    }

    /**
     *Transform list of [Payment] to list of [PaymentListItemModel.PaymentItemModel]
     */
    private fun List<Payment>.toPaymentItemModelList(shotCategory: Boolean,): List<PaymentListItemModel> {
        return this.map { it.toPaymentListItem(shotCategory) }
    }

    /**
     *Transform [Payment] to [PaymentListItemModel.PaymentItemModel]
     */
    private fun Payment.toPaymentListItem(shotCategory: Boolean,): PaymentListItemModel {
        return PaymentListItemModel.PaymentItemModel(this, shotCategory, UUIDUtils.getRandomKey)
    }

}
