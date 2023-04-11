package dev.nelson.mot.main.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nelson.mot.main.data.repository.base.CategoryRepository
import dev.nelson.mot.main.data.repository.CategoryRepositoryImpl
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.data.repository.base.PaymentRepository
import dev.nelson.mot.main.data.room.model.category.CategoryDao
import dev.nelson.mot.main.data.room.model.payment.PaymentDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository = CategoryRepositoryImpl(categoryDao)

    @Provides
    @Singleton
    fun providePaymentRepository(paymentDao: PaymentDao): PaymentRepository = PaymentRepositoryImpl(paymentDao)
}