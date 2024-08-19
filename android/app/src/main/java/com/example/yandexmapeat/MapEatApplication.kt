package com.example.yandexmapeat

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import repository.RestaurantRepositoryImpl
import javax.inject.Inject

@HiltAndroidApp
class MapEatApplication : Application() {

    @Inject
    lateinit var repository: RestaurantRepositoryImpl

    /*override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey("8e691497-5f95-489d-862e-b24bd7507b87")

    }*/

    companion object {
        const val MAPKIT_API_KEY = "8e691497-5f95-489d-862e-b24bd7507b87"
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        val config = AppMetricaConfig.newConfigBuilder("3aa75b72-f41e-4ca3-92c9-e578fbbd22de").build()
        AppMetrica.activate(this, config)
        AppMetrica.enableActivityAutoTracking(this)
    }

}