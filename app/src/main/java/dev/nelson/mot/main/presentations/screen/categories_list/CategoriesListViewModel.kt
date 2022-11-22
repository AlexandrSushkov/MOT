package dev.nelson.mot.main.presentations.screen.categories_list

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.domain.use_case.category.DeleteCategoriesUseCase
import dev.nelson.mot.main.domain.use_case.category.GetCategoryListItemsUseCase
import dev.nelson.mot.main.domain.use_case.category.ModifyCategoryAction
import dev.nelson.mot.main.domain.use_case.category.ModifyCategoryUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.MotResult
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.successOr
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    getCategoryListItemsUseCase: GetCategoryListItemsUseCase,
    private val deleteCategoriesUseCase: DeleteCategoriesUseCase,
    private val modifyCategoryUseCase: ModifyCategoryUseCase,
) : BaseViewModel() {

    // state
    /**
     * represent entered name in the edit category dialog.
     */
    val categoryNameState
        get() = _categoryNameState.asStateFlow()
    private val _categoryNameState = MutableStateFlow(TextFieldValue())

    val categoryToEditId
        get() = _categoryToEditId.asStateFlow()
    private val _categoryToEditId = MutableStateFlow<Int?>(null)

    val categories
        get() = _categories.asStateFlow()

    //    private val _categories = MutableStateFlow<List<CategoryListItemModel>>(emptyList())
    private val _categories = MutableStateFlow<MotResult<List<CategoryListItemModel>>>(MotResult.Loading)

    val deleteItemsSnackbarText: Flow<String>
        get() = _deleteItemsSnackbarText.asStateFlow()
    private val _deleteItemsSnackbarText = MutableStateFlow(StringUtils.EMPTY)

    val deletedItemsMessage: Flow<String>
        get() = _deletedItemsMessage.asStateFlow()
    private val _deletedItemsMessage = MutableStateFlow(StringUtils.EMPTY)

    val snackBarVisibilityState
        get() = _snackBarVisibilityState.asStateFlow()
    private val _snackBarVisibilityState = MutableStateFlow(false)

    // actions
    val showEditCategoryDialogAction
        get() = _showEditCategoryDialogAction.asSharedFlow()
    private val _showEditCategoryDialogAction = MutableSharedFlow<Boolean>()

    val showDeletedItemsMessageToast
        get() = _showDeletedItemsMessageToast.asSharedFlow()
    private val _showDeletedItemsMessageToast = MutableSharedFlow<Boolean>()

    // data
    private var initialCategory: Category? = null
    private val initialCategoriesList = mutableListOf<CategoryListItemModel>()
    private var deleteCategoryJob: Job? = null
    private val categoriesToDeleteList = mutableListOf<Category>()

    init {
        viewModelScope.launch {
            getCategoryListItemsUseCase.execute().collect {
                initialCategoriesList.clear()
                initialCategoriesList.addAll(it)
                _categories.value = MotResult.Success(it)
            }
        }
    }

    fun onFavoriteClick(category: Category, isChecked: Boolean) = viewModelScope.launch {
        val checkedCat = Category(category.name, isChecked, category.id)
        modifyCategoryUseCase.execute(checkedCat, ModifyCategoryAction.Edit)
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
            _categoryToEditId.value = category.id
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
            _categoryToEditId.value = null
            _showEditCategoryDialogAction.emit(false)
        }
    }

    private fun addNewCategory() {
        viewModelScope.launch {
            val category = Category(_categoryNameState.value.text)
            modifyCategoryUseCase.execute(category, ModifyCategoryAction.Add)
        }
    }

    private fun editCategory(category: Category) {
        viewModelScope.launch {
            val enteredName = _categoryNameState.value.text
            if (category.name != enteredName) {
                val modifiedCategory = category.copyWith(enteredName)
                modifyCategoryUseCase.execute(modifiedCategory, ModifyCategoryAction.Edit)
            }
        }
    }

    fun onSwipeCategory(categoryItemModel: CategoryListItemModel.CategoryItemModel) {
        // cancel previous jot if exist
        deleteCategoryJob?.cancel()
        // create new one
        deleteCategoryJob = viewModelScope.launch {
            categoriesToDeleteList.add(categoryItemModel.category)
            showSnackBar()
            val temp = mutableListOf<CategoryListItemModel>().apply {
                addAll(_categories.value.successOr(emptyList()))
                remove(categoryItemModel)
            }
            _categories.value = MotResult.Success(temp)
            // ui updated, removed items is not visible on the screen
            // wait
            delay(4000)
            hideSnackBar()
            // remove payments from DB
            deleteCategoriesUseCase.execute(categoriesToDeleteList)
            Timber.e("Deleted: $categoriesToDeleteList")
            showDeletedItemsMessage()
            clearItemsToDeleteList()
        }
    }

    private suspend fun showDeletedItemsMessage() {
        val itemsWord = if (categoriesToDeleteList.size == 1) {
            "item"
        } else {
            "items"
        }
        _deletedItemsMessage.value = "${categoriesToDeleteList.size} $itemsWord deleted"
        _showDeletedItemsMessageToast.emit(true)
        delay(200)
        _showDeletedItemsMessageToast.emit(false)
    }

    fun onUndoDeleteClick() {
        hideSnackBar()
        deleteCategoryJob?.let {
            _categories.value = MotResult.Success(initialCategoriesList)
            clearItemsToDeleteList()
            it.cancel()
        }
    }

    private fun clearItemsToDeleteList() {
        categoriesToDeleteList.clear()
    }

    private fun showSnackBar() {
        val itemsWord = if (categoriesToDeleteList.size == 1) {
            "item"
        } else {
            "items"
        }
        _deleteItemsSnackbarText.value = "${categoriesToDeleteList.size} $itemsWord will be deleted"
        _snackBarVisibilityState.value = true
    }

    private fun hideSnackBar() {
        _snackBarVisibilityState.value = false
    }

}
