package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository){

    fun getCategories(): Flowable<List<CategoryEntity>> = categoryRepository.getCategories()
        .subscribeOn(Schedulers.io())
}