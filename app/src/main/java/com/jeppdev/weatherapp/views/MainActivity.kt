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
import com.jeppdev.weatherapp.viewmodels.WeatherViewModel

class MainActivity : AppCompatActivity(), LocationListener {
    val locationPermissionCode = 2

    private lateinit var temperatureTextView: TextView
    private lateinit var feelsLikeTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var locationManager: LocationManager
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        temperatureTextView = findViewById(R.id.temperature_text)
        feelsLikeTextView = findViewById(R.id.feels_like_text)
        locationTextView = findViewById(R.id.location_text)

        weatherViewModel.getWeather().observe(this, { weather ->
            temperatureTextView.text = "%.2f°C".format(weather.temperature - 273.15)
            feelsLikeTextView.text = "%.2f°C".format(weather.feelsLike - 273.15)
            Log.d("WEATHER_LOG", "Weather changed!")
        })

        getLocation()
        weatherViewModel.updateWeather()
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location?) {
        locationTextView.text = ("Latitude: %s, Longitude: %s".format(location?.latitude, location?.longitude))
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d("WEATHER_LOG", "onStatusChanged: %s (%s)".format(provider, status))
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d("WEATHER_LOG", "onProviderEnabled: %s".format(provider))
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d("WEATHER_LOG", "onProviderDisabled: %s".format(provider))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}