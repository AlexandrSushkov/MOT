package dev.nelson.mot.main

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import dev.nelson.mot.main.di.DaggerAppComponent
import javax.inject.Inject

class MotApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector
    override fun activityInjector(): AndroidInjector<Activity> = activityInjector


    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .injectApp(this)
    }

//    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
//        return DaggerAppComponent.builder().create(this)
//    }
}
