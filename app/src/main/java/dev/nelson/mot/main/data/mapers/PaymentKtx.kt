package dev.nelson.mot.main.data.mapers

import dev.nelson.mot.db.model.category.CategoryEntity
import dev.nelson.mot.db.model.payment.PaymentEntity
import dev.nelson.mot.db.model.paymentjoin.PaymentWithCategoryEntity
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment

fun Payment.copyWith(
    name: String? = null,
    cost: Int? = null,
    dateText: String? = null,
    dateInMills: Long? = null,
    category: Category? = null,
    message: String? = null,
    isSelected: Boolean? = null
): Payment = Payment(
    name = name ?: this.name,
    cost = cost ?: this.cost,
    id = id,
    dateString = dateText ?: this.dateString,
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
        date = dateString,
        dateInMilliseconds = dateInMills,
        categoryIdKey = category?.id,
        summary = message
    )

fun List<Payment>.toPaymentEntityList(): List<PaymentEntity> = this.map { it.toPaymentEntity() }

fun PaymentWithCategoryEntity.toPayment(): Payment {
    val paymentEntity: PaymentEntity = this.paymentEntity
    val categoryEntity: CategoryEntity? = this.categoryEntity

    return with(paymentEntity) {
        Payment(
            name = title,
            cost = cost,
            message = summary.orEmpty(),
            id = id,
            dateString = date,
            dateInMills = dateInMilliseconds ?: 0L,
            category = categoryEntity?.toCategory()
        )
    }
}

fun List<PaymentWithCategoryEntity>.toPaymentList(): List<Payment> = this.map { it.toPayment() }
