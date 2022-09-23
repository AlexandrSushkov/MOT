package dev.nelson.mot.main.domain.use_case

import dev.nelson.mot.main.data.mapers.toCategoryList
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.util.extention.isEven
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

class CategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    fun getAllCategoriesAlphabeticDescFlow(): Flow<List<CategoryListItemModel>> = categoryRepository.getAllCategoriesOrdered()
        .map { it.toCategoryList() }
        .map { it.groupBy { category: Category -> category.name.first() } }
        .map { value: Map<Char, List<Category>> ->
            return@map listOf<CategoryListItemModel>().toMutableList().apply {
                value.forEach { (letter, categoryList) ->
                    add(CategoryListItemModel.Letter(letter.toString(), generateKey()))
                    add(CategoryListItemModel.Empty(generateKey()))
                    addAll(categoryList.map { category -> CategoryListItemModel.CategoryItemModel(category, generateKey()) })
                    if (categoryList.size.isEven().not()) {
                        add(CategoryListItemModel.Empty(generateKey()))
                    }
                }
            }
        }.map {
            it.add(CategoryListItemModel.Footer(generateKey()))
            return@map it
        }

    private fun generateKey() = UUID.randomUUID().toString()


}