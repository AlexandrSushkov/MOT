package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryList
import dev.nelson.mot.main.data.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesOrderedByNameFavoriteFirst @Inject constructor(private val getCategoriesOrderedByName: GetCategoriesOrderedByName) {

    fun execute(isAsc: Boolean = true): Flow<List<Category>> = getCategoriesOrderedByName.execute(isAsc)
        .map { it.toCategoryList() }
        .map { categoryList -> categoryList.sortedBy { category -> !category.isFavorite } } // for some reason this have to be inverted, otherwise favorite items will be in the end of the list

}
