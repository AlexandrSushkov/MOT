package dev.nelson.mot.main.presentations.screen.categories_list

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.R
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.domain.use_case.category.DeleteCategoriesUseCase
import dev.nelson.mot.main.domain.use_case.category.GetCategoryListItemsUseCase
import dev.nelson.mot.main.domain.use_case.category.ModifyCategoryAction
import dev.nelson.mot.main.domain.use_case.category.ModifyCategoryParams
import dev.nelson.mot.main.domain.use_case.category.ModifyCategoryUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.MotUiState
import dev.nelson.mot.db.utils.SortingOrder
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val getCategoryListItemsUseCase: GetCategoryListItemsUseCase,
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

    val titleStringRes
        get() = _title.asStateFlow()
    private val _title = MutableStateFlow(R.string.categories)

    val categoryToEditId
        get() = _categoryToEditId.asStateFlow()
    private val _categoryToEditId = MutableStateFlow<Int?>(null)

    val categoriesResult
        get() = _categoriesResult.asStateFlow()
    private val _categoriesResult =
        MutableStateFlow<MotUiState<List<CategoryListItemModel>>>(MotUiState.Loading)

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
        initialCategoriesLoading()
    }

    fun onFavoriteClick(category: Category, isChecked: Boolean) = launch {
        val checkedCat = Category(category.name, isChecked, category.id)
        val params = ModifyCategoryParams(checkedCat, ModifyCategoryAction.Edit)
        modifyCategoryUseCase.execute(params)
    }

    /**
     * Open [_root_ide_package_.dev.nelson.mot.main.presentations.screen.categories_list.EditCategoryDialog()]
     * by click on categories list item
     *
     * @param category which name will be modified
     */
    fun onCategoryLongPress(category: Category) = launch {
        initialCategory = category
        _categoryToEditId.value = category.id
        _categoryNameState.value =
            TextFieldValue(category.name, selection = TextRange(category.name.length))
        _showEditCategoryDialogAction.emit(true)
    }

    /**
     * Open [_root_ide_package_.dev.nelson.mot.main.presentations.screen.categories_list.EditCategoryDialog()]
     * by click on fab
     *
     */
    fun onAddCategoryClick() = launch {
        _categoryNameState.value = TextFieldValue("")
        _showEditCategoryDialogAction.emit(true)
    }

    fun onSaveCategoryClick() = launch {
        initialCategory?.let { editCategory(it) } ?: addNewCategory()
        _showEditCategoryDialogAction.emit(false)
    }

    fun onNameChanged(textFieldValue: TextFieldValue) {
        _categoryNameState.value = textFieldValue
    }

    fun closeEditCategoryDialog() = launch {
        initialCategory = null
        _categoryToEditId.value = null
        _showEditCategoryDialogAction.emit(false)
    }

    fun onSwipeCategory(categoryItemModel: CategoryListItemModel.CategoryItemModel) {
        // cancel previous jot if exist
        deleteCategoryJob?.cancel()
        // create new one
        deleteCategoryJob = launch {
            categoriesToDeleteList.add(categoryItemModel.category)
            showSnackBar()
            val temp = mutableListOf<CategoryListItemModel>().apply {
                addAll(_categoriesResult.value.successOr(emptyList()))
                remove(categoryItemModel)
            }
            _categoriesResult.value = MotUiState.Success(temp)
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

    fun onUndoDeleteClick() {
        hideSnackBar()
        deleteCategoryJob?.let {
            _categoriesResult.value = MotUiState.Success(initialCategoriesList)
            clearItemsToDeleteList()
            it.cancel()
        }
    }

    private fun initialCategoriesLoading() = launch {
        getCategoryListItemsUseCase.execute(SortingOrder.Ascending).collect {
            delay(500)
            initialCategoriesList.clear()
            initialCategoriesList.addAll(it)
            _categoriesResult.value = MotUiState.Success(it)
        }
    }

    private fun addNewCategory() = launch {
        val category = Category(_categoryNameState.value.text)
        val params = ModifyCategoryParams(category, ModifyCategoryAction.Add)
        modifyCategoryUseCase.execute(params)
    }

    private fun editCategory(category: Category) = launch {
        val enteredName = _categoryNameState.value.text
        if (category.name != enteredName) {
            val modifiedCategory = category.copyWith(enteredName)
            val params = ModifyCategoryParams(modifiedCategory, ModifyCategoryAction.Edit)
            modifyCategoryUseCase.execute(params)
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
