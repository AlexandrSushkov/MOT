package dev.nelson.mot.room.model.payment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface PaymentDao {

    @Query("SELECT * FROM Payments")
    fun getAllPayments(): Flowable<List<Payment>>

    @Insert
    fun addPayments(payments: List<Payment>)
}
