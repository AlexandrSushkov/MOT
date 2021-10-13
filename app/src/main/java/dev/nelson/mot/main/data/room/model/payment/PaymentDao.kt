package dev.nelson.mot.main.data.room.model.payment

import androidx.room.*
import dev.nelson.mot.main.data.room.model.category.CategoryTable
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    //normal
    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} WHERE ${PaymentTable.ID}=:id")
    fun getPaymentById(id: Long): PaymentEntity

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME}")
    fun getPaymentAllPayments(): List<PaymentEntity>

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} INNER JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID}")
    fun getAllPaymentsWithCategory(): List<PaymentWithCategory>
//////////////
    //rx
    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME}")
    fun getAllPaymentsRx(): Flowable<List<PaymentEntity>>

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} INNER JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID}")
    fun getAllPaymentsWithCategoryRx(): Flowable<List<PaymentWithCategory>>

    @Insert
    fun addPayments(paymentEntities: List<PaymentEntity>): Single<List<Long>>
///////////////

    //suspend
    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} ORDER BY ${PaymentTable.ID} DESC")
    suspend fun getAllPaymentsWithCategoryOrderedByIdDescCor(): List<PaymentWithCategory>

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} ORDER BY ${PaymentTable.DATE_IN_MILLISECONDS} DESC")
    suspend fun getAllPaymentsWithCategoryOrderDateDescCor(): List<PaymentWithCategory>

    @Insert
    suspend fun insertPayment(paymentEntity: PaymentEntity)

    @Update
    suspend fun updatePayment(paymentEntity: PaymentEntity)

    @Delete
    suspend fun deletePayment(paymentEntity: PaymentEntity)

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID}")
    suspend fun getAllPaymentsWithCategoryCor(): List<PaymentWithCategory>


    // Flow
    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} ORDER BY ${PaymentTable.DATE_IN_MILLISECONDS} DESC")
    fun getAllPaymentsWithCategoryOrderDateDescFlow(): Flow<List<PaymentWithCategory>>
}
