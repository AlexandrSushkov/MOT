package dev.nelson.mot.main.data.room.model.payment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.nelson.mot.main.data.room.model.payment.Payment
import io.reactivex.Flowable

@Dao
interface PaymentDao {

    @Query("SELECT * FROM Payments")
    fun getAllPayments(): Flowable<List<Payment>>

    @Insert
    fun addPayments(payments: List<Payment>)
}
