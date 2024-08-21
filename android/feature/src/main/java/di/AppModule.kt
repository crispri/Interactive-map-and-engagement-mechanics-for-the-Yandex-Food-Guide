package di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import network.api.YandexMapEatApi
import repository.RestaurantRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRestaurantRepositoryImpl(
        api: YandexMapEatApi,
    ): RestaurantRepositoryImpl {
        return RestaurantRepositoryImpl(api)
    }
}