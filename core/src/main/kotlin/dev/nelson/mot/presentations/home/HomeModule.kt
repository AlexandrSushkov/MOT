package dev.nelson.mot.presentations.home

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.nelson.mot.injection.PerActivity
import dev.nelson.mot.injection.ViewModelKey

@Module
abstract class HomeModule {

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}
