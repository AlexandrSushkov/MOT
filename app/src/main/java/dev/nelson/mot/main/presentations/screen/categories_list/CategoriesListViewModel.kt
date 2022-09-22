package dev.nelson.mot.main.presentations.screen.categories_list

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.domain.use_case.category.AddNewCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.DeleteCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.EditCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.GetAllCategoriesOrderedByNameNew
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    getAllCategoriesOrdered: GetAllCategoriesOrderedByNameNew,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val editCategoryUseCase: EditCategoryUseCase,
    private val addNewCategoryUseCase: AddNewCategoryUseCase
) : BaseViewModel() {

    // state
    /**
     * represent entered name in the edit category dialog.
     */
    val categoryNameState
        get() = _categoryNameState.asStateFlow()
    private val _categoryNameState = MutableStateFlow(TextFieldValue())

    val categoryId
        get() = _categoryId.asStateFlow()
    private val _categoryId = MutableStateFlow<Int?>(null)

    // actions
    val showEditCategoryDialogAction
        get() = _showEditCategoryDialogAction.asSharedFlow()
    private val _showEditCategoryDialogAction = MutableSharedFlow<Boolean>()

    val categoriesFlow: Flow<List<CategoryListItemModel>> = getAllCategoriesOrdered.execute(true)

    private var initialCategory: Category? = null

    fun onFavoriteClick(category: Category, isChecked: Boolean) = viewModelScope.launch {
        val checkedCat = Category(category.name, isChecked, category.id)
        editCategoryUseCase.execute(checkedCat)
    }

    /**
     * Open [_root_ide_package_.dev.nelson.mot.main.presentations.screen.categories_list.EditCategoryDialog()]
     * by click on categories list item
     *
     * @param category which name will be modified
     */
    fun onCategoryLongPress(category: Category) {
        viewModelScope.launch {
            initialCategory = category
            _categoryId.value = category.id
            _categoryNameState.value = TextFieldValue(category.name, selection = TextRange(category.name.length))
            _showEditCategoryDialogAction.emit(true)
        }
    }

    /**
     * Open [_root_ide_package_.dev.nelson.mot.main.presentations.screen.categories_list.EditCategoryDialog()]
     * by click on fab
     *
     */
    fun onAddCategoryClick() {
        viewModelScope.launch {
            _categoryNameState.value = TextFieldValue("")
            _showEditCategoryDialogAction.emit(true)
        }
    }

    fun onSaveCategoryClick() {
        viewModelScope.launch {
            initialCategory?.let { editCategory(it) } ?: addNewCategory()
            _showEditCategoryDialogAction.emit(false)
        }
    }

    fun onNameChanged(textFieldValue: TextFieldValue) {
        _categoryNameState.value = textFieldValue
    }

    fun closeEditCategoryDialog() {
        viewModelScope.launch {
            initialCategory = null
            _categoryId.value = null
            _showEditCategoryDialogAction.emit(false)
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

    private fun deleteCategory(category: Category) = viewModelScope.launch {
        deleteCategoryUseCase.execute(category)
    }

}
