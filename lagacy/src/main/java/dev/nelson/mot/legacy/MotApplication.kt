package dev.nelson.mot.legacy

import android.app.Application
import android.content.Context

class MotApplication : Application(){

    //todo this is temporary. Just to make old code works.
    companion object {
        var context: Context? = null
            private set
    }

}
