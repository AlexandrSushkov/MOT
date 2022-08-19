package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.presentations.categories.CategoryListItemModel
import dev.nelson.mot.main.util.extention.isEven
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllCategoriesOrderedByNameNew @Inject constructor(private val categoryRepository: CategoryRepository) {

    /**
     * @param isAsc - true if order is ascending, false - descending
     */
    fun execute(isAsc: Boolean): Flow<List<CategoryListItemModel>> = categoryRepository.getAllCategoriesOrdered(isAsc)
        .map { it.toCategoryList() }
        .map { it.groupBy { category: Category -> category.name.first().uppercaseChar() } }
        .map { titleCharToCategoryMap: Map<Char, List<Category>> -> createCategoryListViewRepresentation(titleCharToCategoryMap) }

    private fun createCategoryListViewRepresentation(value: Map<Char, List<Category>>): List<CategoryListItemModel> {
        return mutableListOf<CategoryListItemModel>()
            .apply {
                //no category item
                val noCategory = Category("No category")
                add(CategoryListItemModel.CategoryItemModel(noCategory))
                //add categories items
                value.forEach { (letter, categoryList) ->
                    add(CategoryListItemModel.Letter(letter.toString()))
                    add(CategoryListItemModel.Empty)
                    addAll(categoryList.map { category -> category.toCategoryItemModel() })
                    if (categoryList.size.isEven().not()) {
                        add(CategoryListItemModel.Empty)
                    }
                }
                //add footer
                add(CategoryListItemModel.Footer)
            }
    }

    private fun Category.toCategoryItemModel(): CategoryListItemModel.CategoryItemModel {
        return CategoryListItemModel.CategoryItemModel(this)
    }
}
