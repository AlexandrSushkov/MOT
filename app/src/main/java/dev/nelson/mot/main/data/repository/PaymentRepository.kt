package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.payment.Payment
import io.reactivex.Flowable
import javax.inject.Inject

class PaymentRepository @Inject constructor(private val motDatabase: MotDatabase) {

    fun getAllPayments(): Flowable<List<Payment>> = motDatabase.paymentDao().getAllPayments()

}
