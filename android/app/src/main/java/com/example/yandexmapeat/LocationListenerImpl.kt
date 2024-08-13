package com.example.yandexmapeat

import android.util.Log
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus

class LocationListenerImpl() : LocationListener {

    override fun onLocationUpdated(p0: Location) {
        Log.d("Location1111", "latitude = ${p0.position.latitude}")
        Log.d("Location1111", "longitude = ${p0.position.longitude}")
    }

    override fun onLocationStatusUpdated(p0: LocationStatus) {
    }
}