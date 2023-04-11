package dev.nelson.mot.main.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.nelson.mot.main.data.room.MIGRATION_1_2
import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.MotDatabaseInfo
import dev.nelson.mot.main.data.room.model.category.CategoryDao
import dev.nelson.mot.main.data.room.model.payment.PaymentDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): MotDatabase =
        Room.databaseBuilder(context, MotDatabase::class.java, MotDatabaseInfo.NAME)
            .createFromAsset(MotDatabaseInfo.NAME)
            .addMigrations(MIGRATION_1_2)
            .allowMainThreadQueries()
            .build()

    @Provides
    @Singleton
    fun providePaymentDao(motDatabase: MotDatabase): PaymentDao = motDatabase.paymentDao()

    @Provides
    @Singleton
    fun provideCategoryDao(motDatabase: MotDatabase): CategoryDao = motDatabase.categoryDao()
}
