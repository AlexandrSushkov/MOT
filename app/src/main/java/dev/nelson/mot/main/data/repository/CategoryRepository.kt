package dev.nelson.mot.main.data.repository

import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import io.reactivex.Flowable
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val motDatabase: MotDatabase) {

    fun getCategories(): Flowable<List<CategoryEntity>> = motDatabase.categoryDao().getAllCategories()

}
