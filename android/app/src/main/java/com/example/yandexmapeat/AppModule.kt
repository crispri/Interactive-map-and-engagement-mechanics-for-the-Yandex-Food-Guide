package com.example.yandexmapeat

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.RestaurantRepositoryImpl
import network.INetworkRepository
import network.NetworkRepository
import network.api.YandexMapEatApi
import network.datasource.INetworkDatasource
import network.datasource.NetworkDatasource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRestaurantRepositoryImpl(): RestaurantRepositoryImpl {
        return RestaurantRepositoryImpl()
    }
    fun provideNetworkDataSource(api: YandexMapEatApi): INetworkDatasource {
        return NetworkDatasource(api)
    }
    @Provides
    @Singleton
    fun provideNetworkRepository(
        networkDataSource: INetworkDatasource,
    ): INetworkRepository {
        return NetworkRepository(
            networkDataSource
        )
    }
}