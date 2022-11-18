package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.util.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllCategoriesOrderedByNameUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    /**
     * Get list of [CategoryEntity] from DB in a particular order. Transform it to [Category].
     *
     * @param order [Order] represents order
     * @return list of [Category]
     */
    fun execute(order: Order = Order.Ascending): Flow<List<Category>> {
        return when (order) {
            Order.Ascending -> categoryRepository.getAllCategoriesOrderedByNameAscending()
            Order.Descending -> categoryRepository.getAllCategoriesOrderedByNameDescending()
        }.map { it.toCategoryList() }
    }

}
