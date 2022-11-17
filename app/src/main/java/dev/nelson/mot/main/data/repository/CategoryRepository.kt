package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val motDatabase: MotDatabase) {

    fun getAllCategoriesOrderedByNameDescending(): Flow<List<CategoryEntity>> = motDatabase.categoryDao()
        .getAllCategoriesOrderedByNameDescending()

    fun getAllCategoriesOrderedByNameAscending(): Flow<List<CategoryEntity>> = motDatabase.categoryDao()
        .getAllCategoriesOrderedByNameAscending()

    fun getCategory(id: Int): Flow<CategoryEntity> = motDatabase.categoryDao()
        .getCategory(id)

    suspend fun addCategory(category: CategoryEntity) = motDatabase.categoryDao().add(category)

    suspend fun editCategory(category: CategoryEntity) = motDatabase.categoryDao().edit(category)

    suspend fun deleteCategory(category: CategoryEntity) = motDatabase.categoryDao().deleteCategory(category)

    suspend fun deleteCategories(categories: List<CategoryEntity>) = motDatabase.categoryDao().deleteCategories(categories)

}
