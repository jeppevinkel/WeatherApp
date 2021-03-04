package com.jeppdev.weatherapp.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jeppdev.weatherapp.R
import com.jeppdev.weatherapp.viewmodels.GpsViewModel
import com.jeppdev.weatherapp.viewmodels.WeatherViewModel

class MainActivity : AppCompatActivity() {
    val locationPermissionCode = 2

    private lateinit var temperatureTextView: TextView
    private lateinit var feelsLikeTextView: TextView
    private lateinit var locationTextView: TextView
//    private lateinit var locationManager: LocationManager
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val gpsViewModel: GpsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        temperatureTextView = findViewById(R.id.temperature_text)
        feelsLikeTextView = findViewById(R.id.feels_like_text)
        locationTextView = findViewById(R.id.location_text)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 2)
        }

        weatherViewModel.getWeather().observe(this, { weather ->
            temperatureTextView.text = "%.2f°C".format(weather.temperature - 273.15)
            feelsLikeTextView.text = "%.2f°C".format(weather.feelsLike - 273.15)
            Log.d("WEATHER_LOG", "Weather changed!")
        })

        gpsViewModel.getLocation().observe(this, { location ->
            locationTextView.text = ("Latitude: %s\nLongitude: %s".format(location.latitude, location.longitude))
            if (location.latitude != null && location.longitude != null) weatherViewModel.updateWeather(location.latitude, location.longitude)
            Log.d("WEATHER_LOG", "Location changed!")
        })
    }
}