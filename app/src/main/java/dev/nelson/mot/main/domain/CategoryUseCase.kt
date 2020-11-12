package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.repository.CategoryRep
import dev.nelson.mot.main.data.room.model.category.Category
import io.reactivex.Flowable
import javax.inject.Inject

class CategoryUseCase @Inject constructor(private val categoryRep: CategoryRep){

    fun getCategories(): Flowable<List<Category>> = categoryRep.getCategories()
}