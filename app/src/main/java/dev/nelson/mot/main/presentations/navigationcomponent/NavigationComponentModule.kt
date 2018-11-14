package dev.nelson.mot.main.presentations.navigationcomponent

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.nelson.mot.main.di.PerActivity
import dev.nelson.mot.main.di.ViewModelKey

@Module
abstract class NavigationComponentModule {

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(NavigationComponentViewModel::class)
    abstract fun bindViewModel(viewModel: NavigationComponentViewModel): ViewModel
}
