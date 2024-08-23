package di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import network.api.YandexMapEatApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * * Provides a singleton instance of [YandexMapEatApi].
 *
 * This method configures Retrofit with a base URL, a Gson converter for JSON parsing,
 * and an OkHttp client with a logging interceptor for debugging HTTP requests and responses.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideYandexMapEatApi(): YandexMapEatApi {
        return Retrofit.Builder()
            .baseUrl("http://51.250.39.97:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()
            )
            .build()
            .create(YandexMapEatApi::class.java)
    }
}