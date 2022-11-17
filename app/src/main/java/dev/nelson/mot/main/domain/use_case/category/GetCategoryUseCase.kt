package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.presentations.screen.category_details.CategoryDetailsScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Used on [CategoryDetailsScreen] to load [Category] info.
 */
class GetCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    /**
     *  return category by Id
     */
    fun execute(id: Int): Flow<Category> = categoryRepository.getCategory(id)
        .map { it.toCategory() }

}
