package dev.nelson.mot.main.domain.usecase.category

import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.domain.usecase.base.UseCaseFlow
import dev.nelson.mot.main.presentations.screen.paymentdetails.PaymentDetailsScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Used on [PaymentDetailsScreen] to show categories selection list.
 */
class GetCategoriesOrderedByNameFavoriteFirstUseCase @Inject constructor(
    private val getAllCategoriesOrderedByName: GetAllCategoriesOrderedByNameUseCase
) : UseCaseFlow<SortingOrder, List<Category>> {

    override fun execute(params: SortingOrder): Flow<List<Category>> {
        return getAllCategoriesOrderedByName.execute(params)
            .map { categoryList ->
                categoryList.sortedByDescending { category -> category.isFavorite }
            }
    }
}
