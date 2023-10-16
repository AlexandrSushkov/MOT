package dev.nelson.mot.main.domain.usecase.category

import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepositoryImpl
import dev.nelson.mot.main.domain.usecase.base.UseCaseFlow
import dev.nelson.mot.main.presentations.screen.categorydetails.CategoryDetailsScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Used on [CategoryDetailsScreen] to load [Category] info.
 */
class GetCategoryByIdUseCase @Inject constructor(
    private val categoryRepository: CategoryRepositoryImpl
) : UseCaseFlow<Int, Category> {

    /**
     *  return [Category] by given Id
     */
    override fun execute(params: Int): Flow<Category> = categoryRepository.getCategory(params)
        .map { it.toCategory() }
}
