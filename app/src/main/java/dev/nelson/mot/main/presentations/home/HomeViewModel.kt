package dev.nelson.mot.main.presentations.home

import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.domain.TestUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(testUseCase: TestUseCase) : BaseViewModel() {

    val title = ObservableField<String>()
    val onItemClickEvent: SingleLiveEvent<Unit> = SingleLiveEvent()


    init {
        testUseCase.getTest()
            .subscribeBy { title.set(it) }
            .addToDisposables()
    }

}