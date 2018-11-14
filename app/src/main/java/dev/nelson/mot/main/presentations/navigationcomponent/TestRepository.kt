package dev.nelson.mot.main.presentations.navigationcomponent

import android.content.Context
import dev.nelson.mot.main.R
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepository @Inject constructor(private val context: Context) {

    fun test(): Observable<String> = Observable.just(context.getString(R.string.test))
}