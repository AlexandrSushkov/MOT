package dev.nelson.mot.main.data.repository.base

import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategoriesOrderedByNameDescending(): Flow<List<CategoryEntity>>

    fun getAllCategoriesOrderedByNameAscending(): Flow<List<CategoryEntity>>

    fun getCategory(id: Int): Flow<CategoryEntity>

    suspend fun addCategory(category: CategoryEntity)

    suspend fun editCategory(category: CategoryEntity)

    suspend fun deleteCategory(category: CategoryEntity)

    suspend fun deleteCategories(categories: List<CategoryEntity>)
}