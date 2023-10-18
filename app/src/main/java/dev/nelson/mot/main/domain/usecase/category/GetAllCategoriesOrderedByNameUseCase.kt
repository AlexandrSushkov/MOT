package dev.nelson.mot.main.domain.usecase.category

import dev.nelson.mot.db.model.category.CategoryEntity
import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.data.mapers.toCategoryList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepositoryImpl
import dev.nelson.mot.main.domain.usecase.base.UseCaseFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllCategoriesOrderedByNameUseCase @Inject constructor(
    private val categoryRepository: CategoryRepositoryImpl
) : UseCaseFlow<SortingOrder, List<Category>> {

    /**
     * Get list of [CategoryEntity] from DB in a particular order. Transform it to [Category].
     *
     * @param order [SortingOrder] represents order
     * @return list of [Category]
     */
    override fun execute(params: SortingOrder): Flow<List<Category>> {
        return when (params) {
            SortingOrder.Ascending -> categoryRepository.getAllCategoriesOrderedByNameAscending()
            SortingOrder.Descending -> categoryRepository.getAllCategoriesOrderedByNameDescending()
        }.map { categories -> categories.filter { categoryEntity -> categoryEntity.name.isNotEmpty() } }
            .map { it.toCategoryList() }
    }
}
