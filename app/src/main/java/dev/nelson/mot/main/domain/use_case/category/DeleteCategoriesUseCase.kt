package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryEntityList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.base.CategoryRepository
import dev.nelson.mot.main.domain.use_case.UseCaseSuspend
import dev.nelson.mot.main.presentations.screen.categories_list.CategoryListScreen
import javax.inject.Inject

/**
 * Used on [CategoryListScreen] to delete category/categories.
 */
class DeleteCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) : UseCaseSuspend<List<Category>, Unit> {

    override suspend fun execute(params: List<Category>) {
        return categoryRepository.deleteCategories(params.toCategoryEntityList())
    }
}
