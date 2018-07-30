package dev.nelson.mot.presentations.screen

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.nelson.mot.injection.PerActivity
import dev.nelson.mot.injection.ViewModelKey

@Module
abstract class DataBaseTransferModule {

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(DataBaseTransferViewModel::class)
    abstract fun bindsRoomTestViewModel(dataBaseTransferViewModel: DataBaseTransferViewModel): ViewModel

}
