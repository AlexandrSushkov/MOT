package dev.nelson.mot.main.data.mapers

import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory

fun Payment.copyWith(
    name: String? = null,
    cost: Int? = null,
    date: String? = null,
    dateInMills: Long? = null,
    category: Category? = null,
    message: String? = null,
    isSelected: Boolean? = null
): Payment = Payment(
        name = name ?: this.name,
        cost = cost ?: this.cost,
        id = id,
        date = date ?: this.date,
        dateInMills = dateInMills ?: this.dateInMills,
        category = category ?: this.category,
        message = message ?: this.message,
        isSelected = isSelected ?: this.isSelected
    )

fun Payment.toPaymentEntity(): PaymentEntity =
    PaymentEntity(
        name,
        cost,
        id = id,
        date = date,
        dateInMilliseconds = dateInMills,
        categoryIdKey = category?.id,
        summary = message
    )

fun List<Payment>.toPaymentEntityList(): List<PaymentEntity> = this.map { it.toPaymentEntity() }

fun PaymentWithCategory.toPayment(): Payment {
    val paymentEntity: PaymentEntity = this.paymentEntity
    val categoryEntity: CategoryEntity? = this.categoryEntity
    return with(paymentEntity) {
        Payment(
            name = title,
            cost = cost,
            message = summary.orEmpty(),
            id = id,
            date = date,
            dateInMills = dateInMilliseconds,
            category = categoryEntity?.toCategory()
        )
    }
}

fun List<PaymentWithCategory>.toPaymentList(): List<Payment> = this.map { it.toPayment() }
