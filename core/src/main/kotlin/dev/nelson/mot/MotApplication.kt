package dev.nelson.mot

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.v4.app.Fragment
import androidx.fragment.app.Fragment
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import dev.nelson.mot.injection.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class MotApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject lateinit var activityInjector: AndroidInjector<Activity>
    @Inject lateinit var supportFragmentInjector: AndroidInjector<Fragment>

    //todo this is temporary. Just to make old code works.
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

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    private fun initTimber() = Timber.plant(Timber.DebugTree())
    private fun initStetho() = Stetho.initializeWithDefaults(this)
}
