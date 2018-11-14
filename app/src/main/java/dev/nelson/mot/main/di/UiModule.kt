package dev.nelson.mot.main.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.nelson.mot.main.presentations.base.ViewModelFactory
import dev.nelson.mot.main.presentations.navigationcomponent.NavigationComponentActivity
import dev.nelson.mot.main.presentations.navigationcomponent.NavigationComponentModule

@Module
abstract class UiModule{

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @PerActivity
    @ContributesAndroidInjector(modules = [(NavigationComponentModule::class)])
    abstract fun contributeNavigationComponentActivity(): NavigationComponentActivity

}
