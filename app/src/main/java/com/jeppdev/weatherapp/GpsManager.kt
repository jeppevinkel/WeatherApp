package com.jeppdev.weatherapp

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.jeppdev.weatherapp.models.LocationModel
import java.util.*

class GpsManager(private val application: Application) {
    private var _location: LocationModel? = null

    //    private val context: Context = application
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    private val PERMISSION_ID = 2

    init {
        startLocationUpdates()
    }

    fun getLocation() : LocationModel? {
        return _location
    }

    fun checkPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ID)
        }
    }

    private fun startLocationUpdates() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }

                fusedLocationClient.lastLocation.addOnSuccessListener {
                    if (_location?.latitude == it?.latitude && _location?.longitude == it?.longitude) return@addOnSuccessListener
                    _location = LocationModel(it.latitude, it.longitude)
                }
            }}, 0, 10000)
    }
}