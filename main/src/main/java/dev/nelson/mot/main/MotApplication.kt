package dev.nelson.mot.main

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class MotApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            initTimber()
            initStetho()
        }
    }

    private fun initTimber() = Timber.plant(Timber.DebugTree())
    private fun initStetho() = Stetho.initializeWithDefaults(this)
}
