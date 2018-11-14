package dev.nelson.mot.main.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.nelson.mot.main.MotApplication
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: MotApplication): Context = application.applicationContext

}