package dev.nelson.mot.injection.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.nelson.mot.injection.PerActivity
import dev.nelson.mot.presentations.base.ViewModelFactory
import dev.nelson.mot.presentations.screen.RoomTestActivity
import dev.nelson.mot.presentations.screen.RoomTestModule

@Module
abstract class UiModule {

    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @PerActivity
    @ContributesAndroidInjector(modules = [(RoomTestModule::class)])
    abstract fun contributeRoomTestActivity(): RoomTestActivity
}
