package com.jeppdev.weatherapp.viewmodels

import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jeppdev.weatherapp.models.LocationModel
import com.jeppdev.weatherapp.models.WeatherModel

class GpsViewModel(application: Application) : AndroidViewModel(application) {
    private val location = MutableLiveData<LocationModel>()

//    private val context: Context = application
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    val PERMISSION_ID = 2

    init {
        startLocationService()
    }

    fun getLocation() : LiveData<LocationModel> {
        return location
    }

    private fun startLocationService() {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            location.value = LocationModel(it.latitude, it.longitude)
        }
    }
}