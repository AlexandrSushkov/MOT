package dev.nelson.mot.main.presentations.categories_list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.domain.use_case.category.DeleteCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.EditCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.GetAllCategoriesOrderedByName
import dev.nelson.mot.main.domain.use_case.category.GetAllCategoriesOrderedByNameNew
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListComposeViewModel @Inject constructor(
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getAllCategoriesOrdered: GetAllCategoriesOrderedByNameNew,
    private val editCategoryUseCase: EditCategoryUseCase,
) : BaseViewModel() {

    val openCategoryDetailsAction: SingleLiveEvent<Category> = SingleLiveEvent()
    val openPaymentsByCategoryAction: SingleLiveEvent<Category> = SingleLiveEvent()

    val categoriesFlow = getAllCategoriesOrdered.execute(true)

    fun onCategoryClick(category: Category) {
        openPaymentsByCategoryAction.value = category
    }

    fun onCategoryLongClick(category: Category) {
        openCategoryDetailsAction.value = category
    }

    fun onFavoriteClick(category: Category, isChecked: Boolean) = viewModelScope.launch {
        val checkedCat = Category(category.name, isChecked, category.id)
        editCategoryUseCase.execute(checkedCat)
    }


    private fun deleteCategory(category: Category) = viewModelScope.launch {
        deleteCategoryUseCase.execute(category)
    }

}