package dev.nelson.mot.injection.module

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import dev.nelson.mot.room.MotDatabase
import dev.nelson.mot.room.MotDatabaseInfo
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun providesMotDatabase(context: Context): MotDatabase =
            Room.databaseBuilder(context, MotDatabase::class.java, MotDatabaseInfo.NAME)
                    .allowMainThreadQueries()
                    .build()

    @Provides
    @Singleton
    fun provideCategoryDao(database: MotDatabase) = database.categoryDao()
}
