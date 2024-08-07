import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import network.api.YandexMapEatApi
import network.datasource.INetworkDatasource
import network.datasource.NetworkDatasource
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideNetworkDataSource(api: YandexMapEatApi): INetworkDatasource {
//        return NetworkDatasource(api)
//    }
//
//}