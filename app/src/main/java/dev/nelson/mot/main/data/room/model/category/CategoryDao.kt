package dev.nelson.mot.main.data.room.model.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

//    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME}")
//    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME} WHERE ${CategoryTable.ID} = :id")
    fun getCategory(id: Int): Flow<CategoryEntity>

//    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME} ORDER BY CASE WHEN :isAsc = 1 THEN ${CategoryTable.NAME} END COLLATE NOCASE ASC, CASE WHEN :isAsc = 0 THEN ${CategoryTable.NAME} END COLLATE NOCASE DESC")
//    fun getAllCategoriesOrdered(isAsc: Boolean): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME} ORDER BY ${CategoryTable.NAME} ASC")
    fun getAllCategoriesOrderedByNameAscending(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME} ORDER BY ${CategoryTable.NAME} DESC")
    fun getAllCategoriesOrderedByNameDescending(): Flow<List<CategoryEntity>>

    @Insert
    suspend fun add(categoryEntity: CategoryEntity)

    @Update
    suspend fun edit(categoryEntity: CategoryEntity)

    @Delete
    suspend fun deleteCategory(categoryEntity: CategoryEntity)

    @Delete
    suspend fun deleteCategories(categories: List<CategoryEntity>)

}
