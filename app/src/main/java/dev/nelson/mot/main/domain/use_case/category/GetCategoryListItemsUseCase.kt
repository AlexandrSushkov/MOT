package dev.nelson.mot.main.domain.use_case.category

import android.content.res.Resources
import dev.nelson.mot.R
import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.MotListItemModel
import dev.nelson.mot.main.data.repository.base.PaymentRepository
import dev.nelson.mot.main.domain.use_case.base.UseCaseFlow
import dev.nelson.mot.main.presentations.screen.categories_list.CategoryListScreen
import dev.nelson.mot.main.util.UUIDUtils
import dev.nelson.mot.main.util.constant.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Used on [CategoryListScreen] to show all categories.
 */
class GetCategoryListItemsUseCase @Inject constructor(
    private val getAllCategoriesOrderedByName: GetAllCategoriesOrderedByNameUseCase,
    private val paymentRepository: PaymentRepository,
    private val resources: Resources
) : UseCaseFlow<SortingOrder, List<MotListItemModel>> {

    /**
     * Get list of [Category] ordered in a particular order.
     *
     * Transform it to [CategoryListItemModel].
     *
     * @param params [SortingOrder] represents order
     * @return list of [CategoryListItemModel]
     */
    override fun execute(params: SortingOrder): Flow<List<MotListItemModel>> {
        return combine(
            paymentRepository.getAllPaymentsWithoutCategory(),
            getAllCategoriesOrderedByName.execute(params)
        ) { paymentsWithoutCategory, categories ->
            val categoriesGroupedByFirstLetter: Map<Char, List<Category>> =
                categories.groupBy { category: Category ->
                    category.name.first().uppercaseChar()
                }
            createCategoryListViewRepresentation(
                paymentsWithoutCategory.isNotEmpty(),
                categoriesGroupedByFirstLetter
            )
        }
    }

    /**
     * @param isAddEmptyCategory a category for payments without category should be added if payments without category exist.
     */
    private fun createCategoryListViewRepresentation(
        isAddEmptyCategory: Boolean,
        value: Map<Char, List<Category>>
    ): List<MotListItemModel> {
        if (value.isEmpty()) return emptyList()
        return mutableListOf<MotListItemModel>().apply {
            // add no category item
            if (isAddEmptyCategory) {
                val noCategory = Category(
                    resources.getString(R.string.category_payments_without_category),
                    id = Constants.NO_CATEGORY_CATEGORY_ID
                )
                add(MotListItemModel.Item(noCategory, UUIDUtils.randomKey))
            }
            // add categories items
            value.forEach { (letter, categoryList) ->
                add(MotListItemModel.Header(letter.toString(), UUIDUtils.randomKey))
                addAll(categoryList.map { category -> category.toCategoryItemModel() })
            }
            // add footer
            add(MotListItemModel.Footer(UUIDUtils.randomKey))
        }
    }

    private fun Category.toCategoryItemModel(): MotListItemModel.Item {
        return MotListItemModel.Item(this, UUIDUtils.randomKey)
    }
}
