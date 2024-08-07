package com.example.yandexmapeat

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.RestaurantRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRestaurantRepositoryImpl(): RestaurantRepositoryImpl {
        return RestaurantRepositoryImpl()
    }

}