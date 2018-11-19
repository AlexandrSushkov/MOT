package dev.nelson.mot.main.presentations.home

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.presentations.navigationcomponent.TestUseCase
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class HomeViewModel @Inject constructor(testUseCase: TestUseCase) : BaseViewModel(){

    val testString = ObservableField<String>("as")
    val title = ObservableField<String>("title")
    val titleVbilityisis = ObservableBoolean()


    init {
        testUseCase.test()
                .subscribeBy { testString.set(it) }
                .addTo(disposables)
    }

}