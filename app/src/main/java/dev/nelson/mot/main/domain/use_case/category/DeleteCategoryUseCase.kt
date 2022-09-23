package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryEntityList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend fun execute(categories: List<Category>) = categoryRepository.deleteCategories(categories.toCategoryEntityList())
}
