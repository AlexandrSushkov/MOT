package dev.nelson.mot.main.presentations.screen.categories

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.R
import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.MotListItemModel
import dev.nelson.mot.main.domain.usecase.category.DeleteCategoriesUseCase
import dev.nelson.mot.main.domain.usecase.category.GetCategoryListItemsUseCase
import dev.nelson.mot.main.domain.usecase.category.ModifyCategoryAction
import dev.nelson.mot.main.domain.usecase.category.ModifyCategoryParams
import dev.nelson.mot.main.domain.usecase.category.ModifyCategoryUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.MotUiState
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.successOr
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val getCategoryListItemsUseCase: GetCategoryListItemsUseCase,
    private val deleteCategoriesUseCase: DeleteCategoriesUseCase,
    private val modifyCategoryUseCase: ModifyCategoryUseCase
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
        MutableStateFlow<MotUiState<List<MotListItemModel>>>(MotUiState.Loading)

    val deleteItemsCount: Flow<Int>
        get() = _deleteItemsCount.asStateFlow()
    private val _deleteItemsCount = MutableStateFlow(Constants.ZERO)

    val snackBarVisibilityState
        get() = _snackBarVisibilityState.asStateFlow()
    private val _snackBarVisibilityState = MutableStateFlow(false)

    // actions
    val showEditCategoryDialogAction
        get() = _showEditCategoryDialogAction.asSharedFlow()
    private val _showEditCategoryDialogAction = MutableSharedFlow<Boolean>()

    // data
    private var initialCategory: Category? = null
    private val initialCategoriesList = mutableListOf<MotListItemModel>()
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

    fun onCategorySwiped(categoryItemModel: MotListItemModel.Item) {
        // cancel previous jot if exist
        deleteCategoryJob?.cancel()
        // create new one
        deleteCategoryJob = launch {
            categoriesToDeleteList.add(categoryItemModel.category)
            _deleteItemsCount.update { categoriesToDeleteList.size }
            showSnackBar()
            // first pass is to hide category item
            _categoriesResult.update {
                val items = it.successOr(emptyList())
                val updatedItems = items.map { item ->
                    when (item) {
                        is MotListItemModel.Item -> {
                            if (item.category.id == categoryItemModel.category.id) {
                                item.copy(isShow = false)
                            } else {
                                item
                            }
                        }

                        else -> item
                    }
                }
                MotUiState.Success(updatedItems)
            }

            // second pass is to hide header if there is no visible items between headers
            _categoriesResult.update {
                val items = it.successOr(emptyList())
                val updatedItems = items.mapIndexed { index, item ->
                    when (item) {
                        is MotListItemModel.Header -> {
                            // loop thought items between this and next header.
                            // if all of them is hidden -> hide this header
                            var i = index + 1
                            var subItemsExist = false
                            while (i < items.size && items[i] is MotListItemModel.Item) {
                                if (items[i].isShow) {
                                    subItemsExist = true
                                    break
                                } else {
                                    i++
                                }
                            }
                            if (subItemsExist) {
                                item
                            } else {
                                item.copy(isShow = false)
                            }
                        }

                        else -> item
                    }
                }
                MotUiState.Success(updatedItems)
            }

            // ui updated, removed items is not visible on the screen
            // wait
            delay(4000)
            hideSnackBar()
            // remove payments from DB
            deleteCategoriesUseCase.execute(categoriesToDeleteList)
            Timber.e("Category deleted: $categoriesToDeleteList")
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
//            delay(1500)
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
            val modifiedCategory = category.copy(name = enteredName)
            val params = ModifyCategoryParams(modifiedCategory, ModifyCategoryAction.Edit)
            modifyCategoryUseCase.execute(params)
        }
    }

    private fun clearItemsToDeleteList() {
        categoriesToDeleteList.clear()
    }

    private fun showSnackBar() {
        _snackBarVisibilityState.value = true
    }

    private fun hideSnackBar() {
        _snackBarVisibilityState.value = false
    }
}
