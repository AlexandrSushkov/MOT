package dev.nelson.mot.main.presentations.navigationcomponent

import androidx.databinding.ObservableField
import dev.nelson.mot.main.presentations.base.BaseViewModel
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class NavigationComponentViewModel @Inject constructor(testUseCase: TestUseCase): BaseViewModel(){

    var test = ObservableField<String>()

    init {
        testUseCase.test()
                .subscribeBy { test.set(it) }
                .addTo(disposables)
    }

}