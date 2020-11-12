package dev.nelson.mot.main.presentations.categories

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import dev.nelson.mot.main.data.room.model.category.Category
import dev.nelson.mot.main.domain.CategoryUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import io.reactivex.rxkotlin.addTo

class CategoriesViewModel @ViewModelInject constructor(private val categoryUseCase: CategoryUseCase) : BaseViewModel() {

    val categories = ObservableArrayList<Category>()
    var isShowPlaceholder = ObservableBoolean()


    init {
        categoryUseCase.getCategories()
            .doOnNext {
                categories.addAll(it)
                isShowPlaceholder.set(categories.isEmpty())
            }
            .doOnError { it.printStackTrace() }
            .subscribe()
            .addTo(disposables)
    }
}