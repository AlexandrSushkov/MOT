package dev.nelson.mot.main.data.mapers

import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory

fun Payment.copyWith(name:String, cost: Int, dateInMills: Long?, category: Category?): Payment =
    Payment(
        name,
        cost,
        id = id,
        date = date,
        dateInMills = dateInMills ?: this.dateInMills,
        category = category ?: this.category
    )

fun Payment.toPaymentEntity(): PaymentEntity =
    PaymentEntity(
        name,
        cost,
        id = id,
        date = date,
        dateInMilliseconds = dateInMills,
        categoryIdKey = category?.id
    )

fun PaymentWithCategory.toPayment(): Payment {
    val paymentEntity = this.paymentEntity
    val categoryEntity = this.categoryEntity
    return with(paymentEntity) {
        Payment(title, cost, id, date, dateInMilliseconds, categoryEntity?.toCategory())
    }
}

fun List<PaymentWithCategory>.toPaymentList(): List<Payment> = this.map { it.toPayment() }