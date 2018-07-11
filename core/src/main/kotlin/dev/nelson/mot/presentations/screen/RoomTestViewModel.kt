package dev.nelson.mot.presentations.screen

import android.databinding.ObservableBoolean
import dev.nelson.mot.interactor.RoomTestInteractor
import dev.nelson.mot.presentations.base.BaseViewModel
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class RoomTestViewModel @Inject constructor(roomTestInteractor: RoomTestInteractor): BaseViewModel() {

    val isShowText = ObservableBoolean()

    init {
        roomTestInteractor.initTransfer()
                .subscribeBy(onComplete = { Timber.e("success!")},
                        onError = {handleBaseError(it)})
                .addTo(disposables)
    }

}
