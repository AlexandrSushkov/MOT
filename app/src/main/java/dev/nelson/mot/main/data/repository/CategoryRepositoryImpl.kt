package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.repository.base.CategoryRepository
import dev.nelson.mot.db.model.category.CategoryDao
import dev.nelson.mot.db.model.category.CategoryEntity
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

    override suspend fun addCategory(category: CategoryEntity) {
        return categoryDao.add(category)
    }

    override suspend fun editCategory(category: CategoryEntity) {
        return categoryDao.edit(category)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        return categoryDao.deleteCategory(category)
    }

    override suspend fun deleteCategories(categories: List<CategoryEntity>) {
        return categoryDao.deleteCategories(categories)
    }
}
