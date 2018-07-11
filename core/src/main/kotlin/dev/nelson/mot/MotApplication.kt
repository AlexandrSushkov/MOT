package dev.nelson.mot

import android.app.Application
import android.content.Context

import com.facebook.stetho.Stetho

class MotApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MotApplication.context = applicationContext
        Stetho.initializeWithDefaults(this)
    }

    companion object {
        var context: Context? = null
            private set
    }
}
