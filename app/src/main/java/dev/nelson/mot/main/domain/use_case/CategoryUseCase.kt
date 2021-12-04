package dev.nelson.mot.main.domain.use_case

import dev.nelson.mot.main.data.repository.CategoryRepository
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.presentations.categories.CategoryListItemModel
import dev.nelson.mot.main.util.extention.isEven
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

//    fun getCategories(): Flowable<List<CategoryEntity>> = categoryRepository.getCategories()
//        .subscribeOn(Schedulers.io())
//
//    fun getCategoriesFlow(): Flow<List<CategoryEntity>> = categoryRepository.getCategoriesFlow()
//
//    fun getAllCategoriesAlphabeticDescFlow(): Flow<List<CategoryListItemModel>> = categoryRepository.getAllCategoriesOrdered()
//        .map { it.groupBy { category: CategoryEntity -> category.name.first() } }
//        .map { value: Map<Char, List<CategoryEntity>> ->
//            return@map listOf<CategoryListItemModel>().toMutableList().apply {
//                value.forEach { (letter, categoryList) ->
//                    add(CategoryListItemModel.Letter(letter.toString()))
//                    add(CategoryListItemModel.Empty)
//                    addAll(categoryList.map { categoryEntity -> CategoryListItemModel.CategoryItemModel(categoryEntity) })
//                    if (categoryList.size.isEven().not()) {
//                        add(CategoryListItemModel.Empty)
//                    }
//                }
//            }
//        }.map {
//            it.add(CategoryListItemModel.Footer)
//            return@map it
//        }

}