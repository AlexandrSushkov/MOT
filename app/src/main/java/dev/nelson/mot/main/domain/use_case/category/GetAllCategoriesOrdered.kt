package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.presentations.categories.CategoryListItemModel
import dev.nelson.mot.main.util.extention.isEven
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllCategoriesOrdered @Inject constructor(private val categoryRepository: CategoryRepository) {

    fun execute(isAsc: Boolean): Flow<List<CategoryListItemModel>> = categoryRepository.getAllCategoriesOrdered(isAsc)
        .map { it.groupBy { category: CategoryEntity -> category.name.first().uppercaseChar() } }
        .map { titleCharToCategoryMap: Map<Char, List<CategoryEntity>> -> createCategoryListViewRepresentation(titleCharToCategoryMap) }

    private fun createCategoryListViewRepresentation(value: Map<Char, List<CategoryEntity>>): List<CategoryListItemModel> {
        return listOf<CategoryListItemModel>()
            .toMutableList()
            .apply {
                //no category item
                val noCategory = CategoryEntity("No category")
                add(CategoryListItemModel.CategoryItemModel(noCategory))
                add(CategoryListItemModel.Empty)
                //add categories items
                value.forEach { (letter, categoryList) ->
                    add(CategoryListItemModel.Letter(letter.toString()))
                    add(CategoryListItemModel.Empty)
                    addAll(categoryList.map { categoryEntity -> categoryEntity.toCategoryItemModel() })
                    if (categoryList.size.isEven().not()) {
                        add(CategoryListItemModel.Empty)
                    }
                }
                //add footer
                add(CategoryListItemModel.Footer)
            }
    }

    fun CategoryEntity.toCategoryItemModel(): CategoryListItemModel.CategoryItemModel {
        return CategoryListItemModel.CategoryItemModel(this)
    }

}