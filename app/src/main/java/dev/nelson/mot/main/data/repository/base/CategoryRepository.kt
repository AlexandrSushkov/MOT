package dev.nelson.mot.main.data.repository.base

import dev.nelson.mot.db.model.category.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategoriesOrderedByNameDescending(): Flow<List<CategoryEntity>>

    fun getAllCategoriesOrderedByNameAscending(): Flow<List<CategoryEntity>>

    fun getCategory(id: Int): Flow<CategoryEntity>

    suspend fun getAllCategories(): List<CategoryEntity>

    suspend fun addCategory(category: CategoryEntity)

    suspend fun editCategory(category: CategoryEntity)

    suspend fun editCategories(category: List<CategoryEntity>)

    suspend fun deleteCategory(category: CategoryEntity)

    suspend fun deleteCategories(categories: List<CategoryEntity>)
}