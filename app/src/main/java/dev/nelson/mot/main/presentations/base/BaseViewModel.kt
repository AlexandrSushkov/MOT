package dev.nelson.mot.main.presentations.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel() {

    val hideKeyboardAction
        get() = _hideKeyboardAction.asStateFlow()
    private val _hideKeyboardAction = MutableStateFlow(Unit)

    val showKeyboardAction
        get() = _showKeyboardAction.asStateFlow()
    private val _showKeyboardAction = MutableStateFlow(Unit)

    val showToastAction
        get() = _showToast.asSharedFlow()
    private val _showToast = MutableSharedFlow<String>()

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    protected fun showToast(message: String) {
        _showToast.tryEmit(message)
    }

    protected fun showKeyboard() {
        _showKeyboardAction.tryEmit(Unit)
    }

    protected fun hideKeyboard() {
        _hideKeyboardAction.tryEmit(Unit)
    }

}
