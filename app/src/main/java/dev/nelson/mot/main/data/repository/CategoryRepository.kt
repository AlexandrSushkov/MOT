package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val motDatabase: MotDatabase) {

    fun getCategories(): Flowable<List<CategoryEntity>> = motDatabase.categoryDao().getAllCategories()

    fun getCategoriesFlow(): Flow<List<CategoryEntity>> = motDatabase.categoryDao().getAllCategoriesFlow()

    fun getAllCategoriesAlphabeticDescFlow(): Flow<List<CategoryEntity>> = motDatabase.categoryDao()
        .getAllCategoriesAlphabeticDescFlow()

    suspend fun deleteCategory(category: CategoryEntity) = motDatabase.categoryDao().deleteCategory(category)

}
