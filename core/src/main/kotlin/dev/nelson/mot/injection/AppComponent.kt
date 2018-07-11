package dev.nelson.mot.injection

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import dev.nelson.mot.MotApplication
import dev.nelson.mot.injection.module.AppModule
import dev.nelson.mot.injection.module.DataModule
import dev.nelson.mot.injection.module.UiModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AndroidInjectionModule::class,
    AppModule::class,
    UiModule::class,
    DataModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MotApplication): Builder

        fun build(): AppComponent
    }

    fun injectApp(app: MotApplication)
}
