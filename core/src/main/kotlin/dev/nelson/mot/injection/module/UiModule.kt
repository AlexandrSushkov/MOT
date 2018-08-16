package dev.nelson.mot.injection.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.nelson.mot.injection.PerActivity
import dev.nelson.mot.presentations.base.ViewModelFactory
import dev.nelson.mot.presentations.home.HomeActivity
import dev.nelson.mot.presentations.home.HomeModule
import dev.nelson.mot.presentations.screen.DataBaseTransferActivity
import dev.nelson.mot.presentations.screen.DataBaseTransferModule

@Module
abstract class UiModule {

    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @PerActivity
    @ContributesAndroidInjector(modules = [(DataBaseTransferModule::class)])
    abstract fun contributeRoomTestActivity(): DataBaseTransferActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(HomeModule::class)])
    abstract fun contributeHomeActivity(): HomeActivity
}
