package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.presentations.screen.categories_list.CategoryListScreen
import dev.nelson.mot.main.util.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

/**
 * Used on [CategoryListScreen] to show all categories.
 */
class GetCategoryListItemsUseCase @Inject constructor(private val getAllCategoriesOrderedByName: GetAllCategoriesOrderedByNameUseCase) {

    /**
     * Get list of [Category] ordered in a particular order.
     *
     * Transform it to [CategoryListItemModel].
     *
     * @param order [Order] represents order
     * @return list of [CategoryListItemModel]
     */
    fun execute(order: Order = Order.Ascending): Flow<List<CategoryListItemModel>> = getAllCategoriesOrderedByName.execute(order)
        .map { it.groupBy { category: Category -> category.name.first().uppercaseChar() } }
        .map { titleCharToCategoryMap: Map<Char, List<Category>> -> createCategoryListViewRepresentation(titleCharToCategoryMap) }

    private fun createCategoryListViewRepresentation(value: Map<Char, List<Category>>): List<CategoryListItemModel> {
        return mutableListOf<CategoryListItemModel>()
            .apply {
                //no category item
                val noCategory = Category("No category")
                add(CategoryListItemModel.CategoryItemModel(noCategory, generateKey()))
                //add categories items
                value.forEach { (letter, categoryList) ->
                    add(CategoryListItemModel.Letter(letter.toString(), generateKey()))
                    addAll(categoryList.map { category -> category.toCategoryItemModel() })
                }
                //add footer
                add(CategoryListItemModel.Footer(generateKey()))
            }
    }

    private fun generateKey() = UUID.randomUUID().toString()

    private fun Category.toCategoryItemModel(): CategoryListItemModel.CategoryItemModel {
        return CategoryListItemModel.CategoryItemModel(this, generateKey())
    }
}
