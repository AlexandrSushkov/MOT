package dev.nelson.mot.main

import android.app.Application

class MotApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
        }
    }
}
