package dev.nelson.mot.db.model.payment

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.nelson.mot.db.model.category.CategoryTable
import dev.nelson.mot.db.model.paymentjoin.PaymentWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Insert
    suspend fun addPayment(paymentEntity: PaymentEntity)

    @Update
    suspend fun updatePayment(paymentEntity: PaymentEntity)

    @Update
    suspend fun updatePayments(paymentEntity: List<PaymentEntity>)

    @Delete
    suspend fun deletePayment(paymentEntity: PaymentEntity)

    @Delete
    suspend fun deletePayments(paymentEntityList: List<PaymentEntity>)

    @Query(
        """
        SELECT * FROM ${PaymentTable.TABLE_NAME} 
        LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} 
        WHERE ${PaymentTable.ID}=:id
    """
    )
    fun getPaymentById(id: Int): Flow<PaymentWithCategory>

    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME}")
    suspend fun getAllPayments(): List<PaymentEntity>

    @Query(
        """
        SELECT * FROM ${PaymentTable.TABLE_NAME} 
        LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID}
    """
    )
    fun getAllPaymentsWithCategory(): Flow<List<PaymentWithCategory>>

    //suspend
    @Query(
        """
        SELECT * FROM ${PaymentTable.TABLE_NAME} 
        LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} 
        ORDER BY 
        CASE WHEN :isAsc = 1 THEN ${PaymentTable.ID} END ASC,
        CASE WHEN :isAsc = 0 THEN ${PaymentTable.ID} END DESC
    """
    )
    fun getAllPaymentsWithCategoryOrderedById(
        isAsc: Boolean
    ): Flow<List<PaymentWithCategory>>

    @Query(
        """
        SELECT * FROM ${PaymentTable.TABLE_NAME} 
        LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} 
        ORDER BY 
        CASE WHEN :isAsc = 1 THEN ${PaymentTable.DATE_IN_MILLISECONDS} END ASC,
        CASE WHEN :isAsc = 0 THEN ${PaymentTable.DATE_IN_MILLISECONDS} END DESC
    """
    )
    fun getAllPaymentsWithCategoryOrderDate(
        isAsc: Boolean
    ): Flow<List<PaymentWithCategory>>

    /**
     * has end time
     */
    @Query(
        """
        SELECT * FROM ${PaymentTable.TABLE_NAME} 
        LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} 
        WHERE ${PaymentTable.DATE_IN_MILLISECONDS} > :startTime 
        AND ${PaymentTable.DATE_IN_MILLISECONDS} < :endTime 
        ORDER BY 
        CASE WHEN :isAsc = 1 THEN ${PaymentTable.DATE_IN_MILLISECONDS} END ASC,
        CASE WHEN :isAsc = 0 THEN ${PaymentTable.DATE_IN_MILLISECONDS} END DESC
    """
    )
    fun getPaymentsWithCategoryByFixedDateRange(
        startTime: Long,
        endTime: Long,
        isAsc: Boolean
    ): Flow<List<PaymentWithCategory>>

    /**
     * doesn't have end time
     */
    @Query(
        """
        SELECT * FROM ${PaymentTable.TABLE_NAME} 
        LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID}
        AND ${PaymentTable.DATE_IN_MILLISECONDS} > :startTime 
        ORDER BY 
        CASE WHEN :isAsc = 1 THEN ${PaymentTable.DATE_IN_MILLISECONDS} END ASC,
        CASE WHEN :isAsc = 0 THEN ${PaymentTable.DATE_IN_MILLISECONDS} END DESC
    """
    )
    fun getPaymentsWithCategoryNoCategoryNoFixedDateRange(
        startTime: Long,
        isAsc: Boolean
    ): Flow<List<PaymentWithCategory>>

    @Query(
        """
        SELECT * FROM ${PaymentTable.TABLE_NAME} 
        LEFT JOIN ${CategoryTable.TABLE_NAME} ON ${PaymentTable.CATEGORY_ID_KEY} = ${CategoryTable.ID} 
        WHERE ${PaymentTable.CATEGORY_ID_KEY} = :categoryId 
        AND ${PaymentTable.DATE_IN_MILLISECONDS} > :startTime 
        ORDER BY 
        CASE WHEN :isAsc = 1 THEN ${PaymentTable.DATE_IN_MILLISECONDS} END ASC,
        CASE WHEN :isAsc = 0 THEN ${PaymentTable.DATE_IN_MILLISECONDS} END DESC
    """
    )
    fun getPaymentsWithCategoryByCategoryIdNoFixedDateRange(
        startTime: Long,
        categoryId: Int,
        isAsc: Boolean
    ): Flow<List<PaymentWithCategory>>

    //get payments without category
    @Query("SELECT * FROM ${PaymentTable.TABLE_NAME} WHERE ${PaymentTable.CATEGORY_ID_KEY} IS NULL")
    fun getAllPaymentsWithoutCategory(): Flow<List<PaymentWithCategory>>
}
