package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend fun execute(category: CategoryEntity) = categoryRepository.deleteCategory(category)
}