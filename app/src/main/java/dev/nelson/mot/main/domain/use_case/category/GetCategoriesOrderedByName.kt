package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesOrderedByName @Inject constructor(private val categoryRepository: CategoryRepository){

    fun execute(isAsc: Boolean = true): Flow<List<CategoryEntity>> = categoryRepository.getAllCategoriesOrdered(isAsc)
}