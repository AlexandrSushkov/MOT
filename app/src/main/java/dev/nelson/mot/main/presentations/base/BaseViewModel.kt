package dev.nelson.mot.main.presentations.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e -> handleThrowable(e) }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + coroutineExceptionHandler

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

    protected fun handleThrowable(exception: Throwable) {
        val error = "${Throwable::class.java}: ${exception.message}"
        showToast(error)
        Timber.e(error)
    }

}
