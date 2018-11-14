package dev.nelson.mot.main.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.nelson.mot.main.MotApplication
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class,
            UiModule::class,
            AndroidSupportInjectionModule::class,
            AndroidInjectionModule::class
        ])
interface AppComponent : AndroidInjector<MotApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MotApplication): Builder

        fun build(): AppComponent
    }

    fun injectApp(app: MotApplication)
}