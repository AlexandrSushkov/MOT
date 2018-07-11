package dev.nelson.mot.room.model.payment

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface PaymentDao {

    @Query("SELECT * FROM Payments")
    fun getAllPayments(): Flowable<List<Payment>>

    @Insert
    fun addPayments(payments: List<Payment>)
}
