package dev.nelson.mot.main.presentations.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.domain.use_case.category.DeleteCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.EditCategoryUseCase
import dev.nelson.mot.main.domain.use_case.category.GetAllCategoriesOrderedByName
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListComposeViewModel @Inject constructor(
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getAllCategoriesOrdered: GetAllCategoriesOrderedByName,
    private val editCategoryUseCase: EditCategoryUseCase,
) : BaseViewModel() {

//    val categories = ObservableArrayList<CategoryEntity>()
//    val isLoading = ObservableBoolean()
//    var isShowPlaceholder = ObservableBoolean()
//    val onScrollChanged: Relay<Int> = PublishRelay.create()

//    val toolbarElevation = ObservableField<Int>()

//    private val onCategoryItemClickAction: Relay<CategoryEntity> = PublishRelay.create()
//    private val onCategoryItemLongClickAction: Relay<CategoryEntity> = PublishRelay.create()
//    private val onSwipeToDeleteAction: Relay<CategoryEntity> = PublishRelay.create()


//    private val _adapter = CategoryAdapter(onCategoryItemClickAction, onCategoryItemLongClickAction, onSwipeToDeleteAction)
//    val categoryAdapter = ObservableField(_adapter)
    val swipeToDeleteCallback: MutableLiveData<CategorySwipeToDeleteCallback> = MutableLiveData()
    val openCategoryDetailsAction: SingleLiveEvent<CategoryEntity> = SingleLiveEvent()
    val openPaymentsByCategoryAction: SingleLiveEvent<CategoryEntity> = SingleLiveEvent()

    val categoriesFlow = getAllCategoriesOrdered.execute(true)

    init {
//        val swipeToDeleteCallback = CategorySwipeToDeleteCallback(
//            _adapter,
//            ItemTouchHelper.ACTION_STATE_IDLE,
//            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        )
//        this.swipeToDeleteCallback.value = swipeToDeleteCallback

        viewModelScope.launch {
//            val list= listOf<CategoryListItemModel>().toMutableList()
//            list.add(CategoryListItemModel.Header)
//            _adapter.setData(listOf(CategoryListItemModel.Header, CategoryListItemModel.Empty))
//            delay(3000)
//            categoryUseCase.getCategoriesFlow()
//                .map { categoryList -> categoryList.map { CategoryListItemModel.CategoryItemModel(it) } }
//                .map {
//                    val result = listOf<CategoryListItemModel>().toMutableList()
//                    result.addAll(it)
//                    if (!result.size.isEven()){
//                        result.add(CategoryListItemModel.Empty)
//                    }
//                    result.add(CategoryListItemModel.Footer)
//                    return@map result
//                }
//                .collect { _adapter.setData(it) }

//            getAllCategoriesOrdered.execute(true)
//                .collect { _adapter.setData(it) }


        }

//        onSwipeToDeleteAction
//            .doOnNext { deleteCategory(it) }
//            .subscribe()
//            .addToDisposables()
//
//        onCategoryItemClickAction
//            .doOnNext { openPaymentsByCategoryAction.value = it }
//            .subscribe()
//            .addToDisposables()
//
//        onCategoryItemLongClickAction
//            .doOnNext { openCategoryDetailsAction.value = it }
//            .subscribe()
//            .addToDisposables()

//        onScrollChanged
//            .distinctUntilChanged()
//            .doOnNext { if (it == 0) toolbarElevation.set(0) else toolbarElevation.set(20) }
//            .subscribe()
//            .addToDisposables()
    }

    fun onCategoryClick(category: CategoryEntity) {
        openPaymentsByCategoryAction.value = category
    }

    fun onCategoryLongClick(category: CategoryEntity) {
        openCategoryDetailsAction.value = category
    }

    fun onFavoriteClick(category: CategoryEntity, isChecked: Boolean) {
        viewModelScope.launch {
            val checkedCat = CategoryEntity(category.name, if(isChecked) 1 else 0 , category.id)
            editCategoryUseCase.execute(checkedCat.toCategory())
        }
    }

    private fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            deleteCategoryUseCase.execute(category)
        }
    }
}