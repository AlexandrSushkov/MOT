package dev.nelson.mot

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.content.Context
import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasFragmentInjector
import dev.nelson.mot.injection.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class MotApplication : Application(), HasActivityInjector, HasFragmentInjector {


    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    companion object {
        var context: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        MotApplication.context = applicationContext
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .injectApp(this)

        if (BuildConfig.DEBUG) {
            initTimber()
            initStetho()
        }
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = activityInjector
    override fun fragmentInjector(): DispatchingAndroidInjector<Fragment> = fragmentInjector


    private fun initTimber() = Timber.plant(Timber.DebugTree())
    private fun initStetho() = Stetho.initializeWithDefaults(this)
}
