package dev.nelson.mot.main.data.repository

import android.content.Context
import io.reactivex.Observable
import javax.inject.Inject

class TestRepository @Inject constructor(context: Context) {

    fun test(): Observable<String> = Observable.just("Movies")

}
