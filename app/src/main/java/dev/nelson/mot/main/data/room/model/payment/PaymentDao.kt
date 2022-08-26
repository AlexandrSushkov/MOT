package dev.nelson.mot.main.data.room.model.payment

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.nelson.mot.main.data.room.model.category.CategoryTable
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} WHERE ${PaymentTable.ID}=:id")
    fun getPaymentById(id: Int): Flow<PaymentWithCategory>

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID}")
    fun getAllPaymentsWithCategory(): List<PaymentWithCategory>

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

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} WHERE ${CategoryTable.ID} = :categoryEntityId ORDER BY ${PaymentTable.DATE_IN_MILLISECONDS} DESC")
    fun getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(categoryEntityId: Int): Flow<List<PaymentWithCategory>>

    //get payments without category
    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} WHERE ${PaymentTable.CATEGORY_ID_KEY} IS NULL")
    fun getAllPaymentsWithoutCategory(): Flow<List<PaymentWithCategory>>
}
