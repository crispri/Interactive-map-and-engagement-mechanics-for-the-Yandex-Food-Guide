package com.example.yandexmapeat

import android.app.Application
import android.location.LocationManager
import android.os.Bundle
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
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

}