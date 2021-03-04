package com.jeppdev.weatherapp.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jeppdev.weatherapp.models.WeatherModel

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val weather = MutableLiveData<WeatherModel>()
    val queue = Volley.newRequestQueue(application.applicationContext)

    init {
        //weather.value = WeatherModel(273.15, 273.15, 0)
        //updateWeather()
    }

    fun updateWeather() {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=Odense,Denmark&appid=254b060232f8bc0ce1f558683ba8d5dc"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("WEATHER_LOG", "Response: %s".format(response.toString()))
                weather.value = WeatherModel(
                    response.getJSONObject("main").getDouble("temp"),
                    response.getJSONObject("main").getDouble("feels_like"),
                    response.getJSONArray("weather").getJSONObject(0).getInt("id")
                )

                Log.i("WEATHER_LOG", "Temperature: %s, Feels like: %s".format(response.getJSONObject("main").getDouble("temp"), response.getJSONObject("main").getDouble("feels_like")))
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
                weather.value = WeatherModel(
                    response.getJSONObject("main").getDouble("temp"),
                    response.getJSONObject("main").getDouble("feels_like"),
                    response.getJSONArray("weather").getJSONObject(0).getInt("id")
                )

                Log.i("WEATHER_LOG", "Temperature: %s, Feels like: %s".format(response.getJSONObject("main").getDouble("temp"), response.getJSONObject("main").getDouble("feels_like")))
            },
            { error ->
                Log.e("WEATHER_LOG", error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun getWeather() : LiveData<WeatherModel> {
        return weather
    }
}