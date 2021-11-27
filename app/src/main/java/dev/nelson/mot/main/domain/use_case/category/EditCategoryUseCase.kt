package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryEntity
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepository
import javax.inject.Inject

class EditCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend fun execute(category: Category) = categoryRepository.editCategory(category.toCategoryEntity())
}
