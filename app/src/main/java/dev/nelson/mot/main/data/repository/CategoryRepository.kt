package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val motDatabase: MotDatabase) {

    fun getCategories(): Flowable<List<CategoryEntity>> = motDatabase.categoryDao().getAllCategories()

    fun getCategoriesFlow(): Flow<List<CategoryEntity>> = motDatabase.categoryDao().getAllCategoriesFlow()

    fun getAllCategoriesOrdered(isAsc: Boolean = true): Flow<List<CategoryEntity>> = motDatabase.categoryDao()
        .getAllCategoriesOrdered(isAsc)

    suspend fun deleteCategory(category: CategoryEntity) = motDatabase.categoryDao().deleteCategory(category)

    suspend fun addNewCategory(category: CategoryEntity) = motDatabase.categoryDao().add(category)

    suspend fun editCategory(category: CategoryEntity) = motDatabase.categoryDao().edit(category)

}
