package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import io.reactivex.Flowable
import javax.inject.Inject

class PaymentRepository @Inject constructor(private val motDatabase: MotDatabase) {

    fun getAllPayments(): Flowable<List<PaymentEntity>> = motDatabase.paymentDao().getAllPayments()

    fun getAllPaymentsWithCategory(): Flowable<List<PaymentWithCategory>> = motDatabase.paymentDao().getAllPaymentsWithCategory()

}
