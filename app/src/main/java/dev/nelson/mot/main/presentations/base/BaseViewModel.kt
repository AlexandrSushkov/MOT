package dev.nelson.mot.main.presentations.base

import androidx.lifecycle.ViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

abstract class BaseViewModel : ViewModel() {

    val hideKeyboardAction = SingleLiveEvent<Unit>()
    val showKeyboardAction = SingleLiveEvent<Unit>()

    private val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    protected fun Disposable.addToDisposables()= this.addTo(disposables)

    protected fun Disposable.removeFromDisposables()= disposables.remove(this)

    protected fun showKeyboard(){
        showKeyboardAction.call()
    }

    protected fun hideKeyboard(){
        hideKeyboardAction.call()
    }

}
