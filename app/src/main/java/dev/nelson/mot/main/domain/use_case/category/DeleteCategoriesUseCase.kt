package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryEntityList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.presentations.screen.categories_list.CategoryListScreen
import javax.inject.Inject

/**
 * Used on [CategoryListScreen] to delete category/categories.
 */
class DeleteCategoriesUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend fun execute(categories: List<Category>) = categoryRepository.deleteCategories(categories.toCategoryEntityList())
}
