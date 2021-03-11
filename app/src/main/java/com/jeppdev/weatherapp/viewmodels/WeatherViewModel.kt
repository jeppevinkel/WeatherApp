package com.jeppdev.weatherapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jeppdev.weatherapp.GpsManager
import com.jeppdev.weatherapp.database.WeatherData
import com.jeppdev.weatherapp.database.AppDatabase
import java.util.*

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var db: AppDatabase
    val gpsManager = GpsManager(application)

    private val weather = MutableLiveData<WeatherData>()

    val queue = Volley.newRequestQueue(application.applicationContext)

    init {
        db = AppDatabase.getAppDatabase(application)!!

        weather.value = db.weatherDataDao().getOrCreate()
        Log.d("WEATHER_LOG", "Initial stored: %s (%s)".format(weather.value?.feelsLike.toString(), weather.value?.weatherId))

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val location = gpsManager.getLocation() ?: return
//                updateWeather(location.latitude, location.longitude)
                updateWeather()
            }}, 0, 10000)
    }

    fun updateWeather() {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=Odense,Denmark&appid=254b060232f8bc0ce1f558683ba8d5dc"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("WEATHER_LOG", "Response: %s".format(response.toString()))

                val weatherData: WeatherData = WeatherData(response.getJSONObject("weather").getInt("id"), response.getJSONObject("main").getDouble("feels_like"))

                weather.value = weatherData

                db.weatherDataDao().insertOrUpdate(weatherData)
            },
            { error ->
                Log.e("WEATHER_LOG", error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun updateWeather(latitude: Double, longitude: Double) {
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&appid=254b060232f8bc0ce1f558683ba8d5dc"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("WEATHER_LOG", "Response: %s".format(response.toString()))

                val weatherData: WeatherData = WeatherData(response.getJSONObject("weather").getInt("id"), response.getJSONObject("main").getDouble("feels_like"))

                weather.value = weatherData

                db.weatherDataDao().insertOrUpdate(weatherData)
            },
            { error ->
                Log.e("WEATHER_LOG", error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun getWeather() : LiveData<WeatherData> {
        return weather
    }
}