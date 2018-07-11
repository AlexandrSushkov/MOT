package dev.nelson.mot.presentations.screen

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.nelson.mot.injection.PerActivity
import dev.nelson.mot.injection.ViewModelKey

@Module
abstract class RoomTestModule {

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(RoomTestViewModel::class)
    abstract fun bindsRoomTestViewModel(roomTestViewModel: RoomTestViewModel): ViewModel

}
