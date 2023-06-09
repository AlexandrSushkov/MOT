package dev.nelson.mot.main.presentations.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e -> handleThrowable(e) }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + coroutineExceptionHandler

    //actions
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

    /**
     * Used to store the current first visible item position.
     * @see isScreenContentScrolling
     */
    private val _newFirstVisibleItemPositionState = MutableStateFlow(0)

    /**
     * Used to store the previous first visible item position.
     * @see isScreenContentScrolling
     */
    private val _savedFirstVisibleItemPositionState = MutableStateFlow(0)

    /**
     *  Used to notify appbar that the content is scrolling.
     *  When content is scrolling, appbar color should be changed.
     */
    val isScreenContentScrolling
        get() = _isScreenContentScrolling.asStateFlow()
    private val _isScreenContentScrolling = MutableStateFlow(false)

    init {

        // calculate content is in the middle of scrolling or at the top of the screen
        launch {
            _newFirstVisibleItemPositionState.combine(_savedFirstVisibleItemPositionState) { currentScrollingPosition, savedScrollingPosition ->
                currentScrollingPosition to savedScrollingPosition
            }.collect { (currentScrollingPosition, savedScrollingPosition) ->
                if (currentScrollingPosition == 0 && savedScrollingPosition != 0) {
                    // scrolled to the top. first item is visible. change the color of the toolbar
                    _savedFirstVisibleItemPositionState.value = 0
                    _isScreenContentScrolling.value = false
                    return@collect
                }
                if (currentScrollingPosition != 0 && savedScrollingPosition == 0) {
                    // scroll is started. first item is NOT visible. change the color of the toolbar
                    _savedFirstVisibleItemPositionState.value = currentScrollingPosition
                    _isScreenContentScrolling.value = true
                }
            }
        }
    }

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    /**
     * Set new first visible item position.
     * @see _newFirstVisibleItemPositionState
     * @see _savedFirstVisibleItemPositionState
     * @see isScreenContentScrolling
     */
    protected fun onContentScrollPositionChanged(firstVisibleItemPosition: Int) {
        _newFirstVisibleItemPositionState.value = firstVisibleItemPosition
    }

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
