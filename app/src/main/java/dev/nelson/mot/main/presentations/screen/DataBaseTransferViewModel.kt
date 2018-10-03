package dev.nelson.mot.presentations.screen

import androidx.databinding.ObservableBoolean
import dev.nelson.mot.presentations.base.BaseViewModel

class DataBaseTransferViewModel : BaseViewModel() {

    val isShowText = ObservableBoolean()

    init {
//        roomTestInteractor.initTransfer()
//                .subscribeBy(onComplete = { Timber.e("success!")},
//                        onError = {handleBaseError(it)})
//                .addTo(disposables)
    }

}
