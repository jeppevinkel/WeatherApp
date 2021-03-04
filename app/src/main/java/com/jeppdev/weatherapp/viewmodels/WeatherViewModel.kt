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
import com.jeppdev.weatherapp.database.AppDatabase
import com.jeppdev.weatherapp.database.WeatherData
import com.jeppdev.weatherapp.models.WeatherModel

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    //lateinit var model: WeatherModel
    //var currentWeather: WeatherData? = null

    private lateinit var db: AppDatabase

    private val weather = MutableLiveData<WeatherData>()
    val queue = Volley.newRequestQueue(application.applicationContext)

    init {
//        if(currentWeather == null)
//        {
//            //Create new weather
//            val newWeather = model.getWeather()
//            model.updateWeather(newWeather)
//            currentWeather = newWeather
//        } else {
//            //Get current weather
//            currentWeather = model.getWeather()
//        }
//        currentWeather!!.feelsLike = 273.15

//        weather.value = WeatherModel(273.15, 273.15, 0)

        db = AppDatabase.getAppDatabase(application)!!

        weather.value = db.weatherDataDao().getOrCreate()
        Log.d("WEATHER_LOG", "Initial stored: %s (%s)".format(weather.value?.feelsLike.toString(), weather.value?.weatherId))
    }

    fun updateWeather() {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=Odense,Denmark&appid=254b060232f8bc0ce1f558683ba8d5dc"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("WEATHER_LOG", "Response: %s".format(response.toString()))

                val weatherData: WeatherData = WeatherData(response.getJSONObject("main").getDouble("feels_like"))

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

//                currentWeather!!.feelsLike = response.getJSONObject("main").getDouble("feels_like")
//                model.updateWeather(currentWeather!!)

                val weatherData: WeatherData = WeatherData(response.getJSONObject("main").getDouble("feels_like"))

                weather.value = weatherData

                db.weatherDataDao().insertOrUpdate(weatherData)
            },
            { error ->
                Log.e("WEATHER_LOG", error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }

//    fun getWeather() : WeatherData? {
//        return currentWeather
//    }
    fun getWeather() : LiveData<WeatherData> {
        return weather
    }
}