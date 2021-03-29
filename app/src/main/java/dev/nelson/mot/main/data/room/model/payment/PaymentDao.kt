package dev.nelson.mot.main.data.room.model.payment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import dev.nelson.mot.main.data.room.model.category.CategoryTable
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import io.reactivex.Flowable

@Dao
interface PaymentDao {

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME}")
    fun getAllPayments(): Flowable<List<PaymentEntity>>

    @Transaction
    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} INNER JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID}")
    fun getAllPaymentsWithCategory(): Flowable<List<PaymentWithCategory>>

    @Insert
    fun addPayments(paymentEntities: List<PaymentEntity>)


    //coroutines
    @Transaction
    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} INNER JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID}")
    suspend fun getAllPaymentsWithCategoryCor(): List<PaymentWithCategory>
}
