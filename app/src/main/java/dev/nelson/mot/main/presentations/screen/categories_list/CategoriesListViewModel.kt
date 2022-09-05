package dev.nelson.mot.main.presentations.screen.categories_list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.domain.use_case.category.DeleteCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.EditCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.GetAllCategoriesOrderedByNameNew
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    getAllCategoriesOrdered: GetAllCategoriesOrderedByNameNew,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val editCategoryUseCase: EditCategoryUseCase
) : BaseViewModel() {

    val categoriesFlow: Flow<List<CategoryListItemModel>> = getAllCategoriesOrdered.execute(true)

    fun onFavoriteClick(category: Category, isChecked: Boolean) = viewModelScope.launch {
        val checkedCat = Category(category.name, isChecked, category.id)
        editCategoryUseCase.execute(checkedCat)
    }

    private fun deleteCategory(category: Category) = viewModelScope.launch {
        deleteCategoryUseCase.execute(category)
    }

}
