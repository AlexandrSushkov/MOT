package dev.nelson.mot.main.data.room.model.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.nelson.mot.main.data.room.model.payment.PaymentTable
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME}")
    fun getAllCategories(): Flowable<List<CategoryEntity>>

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME}")
    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME} ORDER BY ${CategoryTable.NAME} ASC")
    fun getAllCategoriesAlphabeticDescFlow(): Flow<List<CategoryEntity>>

    @Insert
    suspend fun add(categoryEntity: CategoryEntity)

    @Update
    suspend fun edit(categoryEntity: CategoryEntity)

    @Delete
    suspend fun deleteCategory(categoryEntity: CategoryEntity)

    @Delete
    suspend fun deleteCategories(categories: List<CategoryEntity>)

    @Insert
    fun addCategories(categorise: List<CategoryEntity>)
}
