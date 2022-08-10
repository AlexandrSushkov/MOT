package dev.nelson.mot.main.presentations.category_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.domain.use_case.category.AddNewCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.EditCategoryUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.StringUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    extras: SavedStateHandle,
    private val addNewCategoryUseCase: AddNewCategoryUseCase,
    private val editCategoryUseCase: EditCategoryUseCase
) : BaseViewModel() {

    private val mode = if ((extras.get<Category>(Constants.CATEGORY_KEY)) == null) SaveCategoryMode.Add else SaveCategoryMode.Edit
    private val category: Category = extras[Constants.CATEGORY_KEY] ?: Category(StringUtils.EMPTY)
    val categoryName = MutableLiveData(category.name)
    val closeScreenAction = SingleLiveEvent<Unit>()

    fun onSaveClick() {
        categoryName.value?.let { newName ->
            if (newName != category.name) {
                val newCategory = category.copyWith(newName)
                when (mode) {
                    is SaveCategoryMode.Add -> addNewCategory(newCategory)
                    is SaveCategoryMode.Edit -> editCategory(newCategory)
                }
            } else {
                closeScreenAction.call()
            }
        }
    }

    private fun addNewCategory(category: Category) {
        viewModelScope.launch {
            addNewCategoryUseCase.execute(category)
            closeScreenAction.call()
        }
    }

    private fun editCategory(category: Category) {
        viewModelScope.launch {
            editCategoryUseCase.execute(category)
            closeScreenAction.call()
        }
    }

    private sealed class SaveCategoryMode {
        object Add : SaveCategoryMode()
        object Edit : SaveCategoryMode()
    }
}
