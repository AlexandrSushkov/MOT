package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.model.category.CategoryDao
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao) {

    fun getAllCategoriesOrderedByNameDescending(): Flow<List<CategoryEntity>> = categoryDao.getAllCategoriesOrderedByNameDescending()

    fun getAllCategoriesOrderedByNameAscending(): Flow<List<CategoryEntity>> = categoryDao.getAllCategoriesOrderedByNameAscending()

    fun getCategory(id: Int): Flow<CategoryEntity> = categoryDao.getCategory(id)

    suspend fun addCategory(category: CategoryEntity) = categoryDao.add(category)

    suspend fun editCategory(category: CategoryEntity) = categoryDao.edit(category)

    suspend fun deleteCategory(category: CategoryEntity) = categoryDao.deleteCategory(category)

    suspend fun deleteCategories(categories: List<CategoryEntity>) = categoryDao.deleteCategories(categories)
}
