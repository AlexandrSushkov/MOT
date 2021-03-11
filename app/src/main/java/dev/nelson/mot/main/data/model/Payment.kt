package dev.nelson.mot.main.data.model

import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory

data class Payment(
    val name: String,
    val cost: Int,
    val id: Int? = null,
    val category: Category? = null,
)

fun PaymentWithCategory.toPayment(): Payment {
    val paymentEntity = this.paymentEntity
    val categoryEntity = this.categoryEntity
    return Payment(paymentEntity.title, paymentEntity.cost, paymentEntity.id, categoryEntity?.toCategory())
}

fun List<PaymentWithCategory>.toPaymentList(): List<Payment> = this.map { it.toPayment() }
