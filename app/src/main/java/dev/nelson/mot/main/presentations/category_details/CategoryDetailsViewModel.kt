package dev.nelson.mot.main.presentations.category_details

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.domain.use_case.category.AddNewCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.EditCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.GetCategoryUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val categoryNameState
        get() = _categoryNameState.asStateFlow()
    private val _categoryNameState = MutableStateFlow(TextFieldValue())

    // actions
    val closeScreenAction
        get() = _closeScreenAction.asSharedFlow()
    private val _closeScreenAction = MutableSharedFlow<Unit>()

    // private data
    private val categoryId: Int? = extras.get<Int>("id")
    private var initialCategory: Category? = null

    init {
        viewModelScope.launch {
            categoryId?.let { categoryId ->
                getCategoryUseCase.execute(categoryId)
                    .collect { category ->
                        initialCategory = category
                        _categoryNameState.value = TextFieldValue(category.name, selection = TextRange(category.name.length))
                    }
            }
        }
    }

    fun onNameChanged(textFieldValue: TextFieldValue){
        _categoryNameState.value = textFieldValue
    }

    fun onSaveClick() {
        viewModelScope.launch {
            initialCategory?.let { editCategory(it) } ?: addNewCategory()
            _closeScreenAction.emit(Unit)
        }
    }

    private fun addNewCategory() {
        viewModelScope.launch {
            val category = Category(_categoryNameState.value.text)
            addNewCategoryUseCase.execute(category)
        }
    }

    private fun editCategory(category: Category) {
        viewModelScope.launch {
            val enteredName = _categoryNameState.value.text
            if (category.name != enteredName) {
                val modifiedCategory = category.copyWith(enteredName)
                editCategoryUseCase.execute(modifiedCategory)
            }
        }
    }
}
