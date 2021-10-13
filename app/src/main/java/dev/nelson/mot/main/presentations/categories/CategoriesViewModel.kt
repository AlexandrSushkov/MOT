package dev.nelson.mot.main.presentations.categories

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.domain.CategoryUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryUseCase: CategoryUseCase
) : BaseViewModel() {

    val categories = ObservableArrayList<CategoryEntity>()
    val isLoading = ObservableBoolean()
    var isShowPlaceholder = ObservableBoolean()
    private val onCategoryEntityItemClickAction: Relay<CategoryEntity> = PublishRelay.create()

    val onScrollChanged: Relay<Int> = PublishRelay.create()

    val toolbarElevation = ObservableField<Int>()
    val onSwipeToDeleteAction: Relay<CategoryEntity> = PublishRelay.create()


    private val _adapter = CategoryAdapter(onCategoryEntityItemClickAction, onSwipeToDeleteAction)
    val categoryAdapter = ObservableField(_adapter)
    val swipeToDeleteCallback: MutableLiveData<CategorySwipeToDeleteCallback> = MutableLiveData()
    val onItemClick: SingleLiveEvent<CategoryEntity> = SingleLiveEvent()

    init {
        val swipeToDeleteCallback = CategorySwipeToDeleteCallback(
            _adapter,
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        )
        this.swipeToDeleteCallback.value = swipeToDeleteCallback

        viewModelScope.launch {
//            val list= listOf<CategoryListItemModel>().toMutableList()
//            list.add(CategoryListItemModel.Header)
            _adapter.setData(listOf(CategoryListItemModel.Header, CategoryListItemModel.Empty))
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

            categoryUseCase.getAllCategoriesAlphabeticDescFlow()
                .collect { _adapter.setData(it) }
        }

        onSwipeToDeleteAction
            .doOnNext { deleteCategory(it) }
            .subscribe()
            .addToDisposables()

        onScrollChanged
            .distinctUntilChanged()
            .doOnNext { if (it == 0) toolbarElevation.set(0) else toolbarElevation.set(20) }
            .subscribe()
            .addToDisposables()

        onCategoryEntityItemClickAction
            .doOnNext { onItemClick.value = it }
            .subscribe()
            .addToDisposables()
    }


    private fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoryUseCase.deleteCategory(category)
        }
    }
}