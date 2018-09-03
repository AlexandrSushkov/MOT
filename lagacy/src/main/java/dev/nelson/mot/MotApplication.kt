package dev.nelson.mot

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import timber.log.Timber

class MotApplication : Application(){

    //todo this is temporary. Just to make old code works.
    companion object {
        var context: Context? = null
            private set
    }

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
