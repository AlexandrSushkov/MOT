package dev.nelson.mot.main.data.repository

import dev.nelson.mot.db.model.category.CategoryDao
import dev.nelson.mot.db.model.category.CategoryEntity
import dev.nelson.mot.main.data.repository.base.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategoriesOrderedByNameDescending(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategoriesOrderedByNameDescending()
    }

    override fun getAllCategoriesOrderedByNameAscending(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategoriesOrderedByNameAscending()
    }

    override fun getCategory(id: Int): Flow<CategoryEntity> {
        return categoryDao.getCategory(id)
    }

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAllCategories()
    }

    override suspend fun addCategory(category: CategoryEntity) {
        categoryDao.addCategory(category)
    }

    override suspend fun editCategory(category: CategoryEntity) {
        categoryDao.editCategory(category)
    }

    override suspend fun editCategories(categories: List<CategoryEntity>) {
        categoryDao.editCategories(categories)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun deleteCategories(categories: List<CategoryEntity>) {
        categoryDao.deleteCategories(categories)
    }
}
