package dev.nelson.mot.main.presentations.movieslist

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.nelson.mot.main.di.ViewModelKey
import dev.nelson.mot.main.di.annotation.PerFragment

@Module
abstract class MovieListModule {

    @Binds
    @IntoMap
    @PerFragment
    @ViewModelKey(MoviesListViewModel::class)
    abstract fun bindViewModel(viewModel: MoviesListViewModel): ViewModel
}
