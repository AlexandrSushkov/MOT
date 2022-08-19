package dev.nelson.mot.main.domain.use_case

import dev.nelson.mot.main.data.mapers.toCategoryList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.presentations.categories.CategoryListItemModel
import dev.nelson.mot.main.util.extention.isEven
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    fun getAllCategoriesAlphabeticDescFlow(): Flow<List<CategoryListItemModel>> = categoryRepository.getAllCategoriesOrdered()
        .map { it.toCategoryList() }
        .map { it.groupBy { category: Category -> category.name.first() } }
        .map { value: Map<Char, List<Category>> ->
            return@map listOf<CategoryListItemModel>().toMutableList().apply {
                value.forEach { (letter, categoryList) ->
                    add(CategoryListItemModel.Letter(letter.toString()))
                    add(CategoryListItemModel.Empty)
                    addAll(categoryList.map { category -> CategoryListItemModel.CategoryItemModel(category) })
                    if (categoryList.size.isEven().not()) {
                        add(CategoryListItemModel.Empty)
                    }
                }
            }
        }.map {
            it.add(CategoryListItemModel.Footer)
            return@map it
        }

}