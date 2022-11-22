package dev.nelson.mot.main.presentations.base

import androidx.lifecycle.ViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel : ViewModel() {

    val hideKeyboardAction = SingleLiveEvent<Unit>()
    val showKeyboardAction = SingleLiveEvent<Unit>()

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    val showToast
        get() = _showToast.asSharedFlow()
    protected val _showToast = MutableSharedFlow<String>()

    protected fun showKeyboard(){
        showKeyboardAction.call()
    }

    protected fun hideKeyboard(){
        hideKeyboardAction.call()
    }

}
