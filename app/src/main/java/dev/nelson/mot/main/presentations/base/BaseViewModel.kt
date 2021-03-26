package dev.nelson.mot.main.presentations.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

abstract class BaseViewModel : ViewModel() {

    private val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    fun handleBaseError(throwable: Throwable) = throwable.printStackTrace()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    protected fun Disposable.addToDisposables()= this.addTo(disposables)

    protected fun Disposable.removeFromDisposables()= disposables.remove(this)
}
