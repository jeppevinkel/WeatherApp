package com.jeppdev.weatherapp.viewmodels

import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.jeppdev.weatherapp.models.LocationModel
import com.jeppdev.weatherapp.models.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class GpsViewModel(application: Application) : AndroidViewModel(application) {
    private val _location = MutableLiveData<LocationModel>()
    private lateinit var locationCallback: LocationCallback

    val tickerChannel = ticker(delayMillis = 10000, initialDelayMillis = 0)

    //    private val context: Context = application
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    val PERMISSION_ID = 2

    init {
        startLocationUpdates()

//        locationRequest = LocationRequest.create()

//        task.addOnSuccessListener { locationSettingsResponse ->
//            locationSettingsResponse.
//        }


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
//                super.onLocationResult(locationResult)

//                for (location in locationResult.locations) {
//                    _location.value = LocationModel(location.latitude, location.longitude)
//                }
            }
        }
    }

    fun getLocation() : LiveData<LocationModel> {
        return _location
    }

    private fun startLocationUpdates() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }

                fusedLocationClient.lastLocation.addOnSuccessListener {
                    if (_location.value?.latitude == it?.latitude && _location.value?.longitude == it?.longitude) return@addOnSuccessListener
                    _location.value = LocationModel(it?.latitude, it?.longitude)
                }
            }}, 0, 10000)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}