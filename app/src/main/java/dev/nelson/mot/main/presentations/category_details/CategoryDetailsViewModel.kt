package dev.nelson.mot.main.presentations.category_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.domain.use_case.category.AddNewCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.EditCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.GetCategoryUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    extras: SavedStateHandle,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val addNewCategoryUseCase: AddNewCategoryUseCase,
    private val editCategoryUseCase: EditCategoryUseCase
) : BaseViewModel() {

    // states
    val categoryState: Flow<Category>
        get() = _category

    // actions
    val closeScreenAction: LiveData<Unit>
        get() = _closeScreen

    // private data
    private val _categoryId: Int? = extras.get<Int>("id")
    private val _mode: SaveCategoryMode = _categoryId?.let { SaveCategoryMode.Edit } ?: SaveCategoryMode.Add
    private val _category: Flow<Category> = _categoryId?.let { getCategoryUseCase.execute(it) } ?: flow { emit(Category.empty()) }
    private val _closeScreen = SingleLiveEvent<Unit>()
    private lateinit var initialCategory: Category

    init {
        viewModelScope.launch {
            _category.collect { initialCategory = it }
        }
    }

    fun onSaveClick(categoryName: String) {
        viewModelScope.launch {
            if (initialCategory.name != categoryName) {
                val newCategory = initialCategory.copyWith(categoryName.trimEnd())
                when (_mode) {
                    is SaveCategoryMode.Add -> addNewCategory(newCategory)
                    is SaveCategoryMode.Edit -> editCategory(newCategory)
                }
            } else {
                _closeScreen.call()
            }
        }
    }

    private fun addNewCategory(category: Category) {
        viewModelScope.launch {
            addNewCategoryUseCase.execute(category)
            _closeScreen.call()
        }
    }

    private fun editCategory(category: Category) {
        viewModelScope.launch {
            editCategoryUseCase.execute(category)
            _closeScreen.call()
        }
    }

    private sealed class SaveCategoryMode {
        object Add : SaveCategoryMode()
        object Edit : SaveCategoryMode()
    }
}
