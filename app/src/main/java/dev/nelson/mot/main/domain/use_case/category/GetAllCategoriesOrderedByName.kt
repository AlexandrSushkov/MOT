package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.util.extention.isEven
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

class GetAllCategoriesOrderedByName @Inject constructor(private val categoryRepository: CategoryRepository) {

    /**
     * Execute
     *
     * @param isAsc - true if order is ascending, false - descending
     * @return
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
