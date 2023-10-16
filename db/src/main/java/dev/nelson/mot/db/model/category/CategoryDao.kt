package dev.nelson.mot.db.model.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME} WHERE ${CategoryTable.ID} = :id")
    fun getCategory(id: Int): Flow<CategoryEntity>

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME}")
    fun getAllCategories(): List<CategoryEntity>

//    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME} ORDER BY CASE WHEN :isAsc = 1 THEN ${CategoryTable.NAME} END COLLATE NOCASE ASC, CASE WHEN :isAsc = 0 THEN ${CategoryTable.NAME} END COLLATE NOCASE DESC")
//    fun getAllCategoriesOrdered(isAsc: Boolean): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME} ORDER BY ${CategoryTable.NAME} ASC")
    fun getAllCategoriesOrderedByNameAscending(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME} ORDER BY ${CategoryTable.NAME} DESC")
    fun getAllCategoriesOrderedByNameDescending(): Flow<List<CategoryEntity>>

    @Insert
    suspend fun addCategory(categoryEntity: CategoryEntity)

    @Update
    suspend fun editCategory(categoryEntity: CategoryEntity)

    @Update
    suspend fun editCategories(categories: List<CategoryEntity>)

    @Delete
    suspend fun deleteCategory(categoryEntity: CategoryEntity)

    @Delete
    suspend fun deleteCategories(categories: List<CategoryEntity>)
}
