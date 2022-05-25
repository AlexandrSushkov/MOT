package dev.nelson.mot.main.data.room.model.payment_tag

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface PaymentTagDao{

    @Query("SELECT * FROM ${PaymentTagTable.TABLE_NAME}")
    fun getAllPaymentTags(): Flowable<List<PaymentTagEntity>>
}
