package dev.nelson.mot.injection.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.nelson.mot.MotApplication
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(app : MotApplication): Context = app.applicationContext

}
