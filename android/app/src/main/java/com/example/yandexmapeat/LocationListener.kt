package com.example.yandexmapeat

import android.util.Log
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus

class LocationListenerImpl : LocationListener {

    override fun onLocationUpdated(p0: Location) {
        Log.e("Location", "latitude = ${p0.position.latitude}")
        Log.e("Location", "longitude = ${p0.position.longitude}")
    }

    override fun onLocationStatusUpdated(p0: LocationStatus) {}
}