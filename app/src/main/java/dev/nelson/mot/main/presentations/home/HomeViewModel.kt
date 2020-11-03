package dev.nelson.mot.main.presentations.home

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import dev.nelson.mot.main.domain.TestUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class HomeViewModel @ViewModelInject constructor(testUseCase: TestUseCase) : BaseViewModel() {

    val title = ObservableField<String>()
    val onItemClickEvent: SingleLiveEvent<Unit> = SingleLiveEvent()


    init {
        testUseCase.getTest()
            .subscribeBy { title.set(it) }
            .addTo(disposables)
    }

    fun openNavComponentActivity(){

    }
}