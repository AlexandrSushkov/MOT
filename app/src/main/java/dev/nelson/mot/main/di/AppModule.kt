package dev.nelson.mot.main.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.MotDatabaseInfo
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideDb(@ApplicationContext context: Context): MotDatabase =
        Room.databaseBuilder(context, MotDatabase::class.java, "mot.db")
            .createFromAsset("mot.db")
            .build()

}
