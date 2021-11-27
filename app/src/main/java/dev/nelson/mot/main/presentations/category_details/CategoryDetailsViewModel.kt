package dev.nelson.mot.main.presentations.category_details

import androidx.databinding.ObservableField
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

    private val mode = if ((extras.get<Category>(Constants.CATEGORY_KEY)) == null) Mode.AddNewCategory else Mode.EditCategory
    private val category: Category = extras[Constants.CATEGORY_KEY] ?: Category(StringUtils.EMPTY)
    val categoryName = ObservableField(category.name)
    val closeAction = SingleLiveEvent<Unit>()

    fun onSaveClick() {
        categoryName.get()?.let { newName ->
            if (newName != category.name) {
                val newCategory = category.copyWith(newName)
                when (mode) {
                    is Mode.AddNewCategory -> addNewCategory(newCategory)
                    is Mode.EditCategory -> editCategory(newCategory)
                }
            } else {
                closeAction.call()
            }
        }
    }

    private fun addNewCategory(category: Category) {
        viewModelScope.launch {
            addNewCategoryUseCase.execute(category)
            closeAction.call()
        }
    }

    private fun editCategory(category: Category) {
        viewModelScope.launch {
            editCategoryUseCase.execute(category)
            closeAction.call()
        }
    }

    private sealed class Mode {
        object AddNewCategory : Mode()
        object EditCategory : Mode()
    }
}