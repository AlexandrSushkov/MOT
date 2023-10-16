package dev.nelson.mot.main.presentations.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e -> handleThrowable(e) }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + coroutineExceptionHandler

    // actions
    val hideKeyboardAction
        get() = _hideKeyboardAction.asSharedFlow()
    private val _hideKeyboardAction = MutableSharedFlow<Unit>()

    val showKeyboardAction
        get() = _showKeyboardAction.asSharedFlow()
    private val _showKeyboardAction = MutableSharedFlow<Unit>()

    // states
    val showToastState
        get() = _showToast.asSharedFlow()
    private val _showToast = MutableSharedFlow<String>()

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    protected suspend fun showToast(message: String) {
        _showToast.emit(message)
    }

    protected suspend fun showKeyboard() {
        _showKeyboardAction.emit(Unit)
    }

    protected suspend fun hideKeyboard() {
        _hideKeyboardAction.emit(Unit)
    }

    protected fun handleThrowable(exception: Throwable) {
        val error = "${Throwable::class.java}: ${exception.message}"
        Timber.e(error)
    }
}
