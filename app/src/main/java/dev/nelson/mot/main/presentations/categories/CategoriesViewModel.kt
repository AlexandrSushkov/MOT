package dev.nelson.mot.main.presentations.categories

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.domain.CategoryUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(categoryUseCase: CategoryUseCase) : BaseViewModel() {

    val categories = ObservableArrayList<CategoryEntity>()
    val isLoading = ObservableBoolean()
    var isShowPlaceholder = ObservableBoolean()

    init {
        Timber.e("initViewModel")
        categoryUseCase.getCategories()
            .doOnSubscribe { isLoading.set(true) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                isLoading.set(false)
                categories.addAll(it)
                isShowPlaceholder.set(categories.isEmpty())
            }
            .doOnError {
                isLoading.set(false)
                it.printStackTrace()
            }
            .subscribe()
            .addToDisposables()
    }
}