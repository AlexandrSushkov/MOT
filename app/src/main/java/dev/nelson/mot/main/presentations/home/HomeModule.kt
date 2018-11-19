package dev.nelson.mot.main.presentations.home

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.nelson.mot.main.di.annotation.PerActivity
import dev.nelson.mot.main.di.ViewModelKey

@Module
abstract class HomeModule {

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel
}
