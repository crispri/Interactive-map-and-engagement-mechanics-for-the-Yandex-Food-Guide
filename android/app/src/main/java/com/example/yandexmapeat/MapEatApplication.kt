package com.example.yandexmapeat

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MapEatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //MapKitFactory.setApiKey("8e691497-5f95-489d-862e-b24bd7507b87")
    }
}