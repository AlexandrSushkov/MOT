package dev.nelson.mot.main.presentations.base

import androidx.lifecycle.ViewModel
import dev.nelson.mot.main.util.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {

    val hideKeyboardAction = SingleLiveEvent<Unit>()
    val showKeyboardAction = SingleLiveEvent<Unit>()

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    protected fun showKeyboard(){
        showKeyboardAction.call()
    }

    protected fun hideKeyboard(){
        hideKeyboardAction.call()
    }

}
