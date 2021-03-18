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
import com.jeppdev.weatherapp.GpsManager
import com.jeppdev.weatherapp.database.WeatherData
import com.jeppdev.weatherapp.models.SettingsModel
import com.jeppdev.weatherapp.models.WeatherModel
import com.jeppdev.weatherapp.database.AppDatabase
import java.util.*

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var weatherModel: WeatherModel
    lateinit var settingsModel: SettingsModel
    var currentWeather: WeatherData? = null
    private lateinit var db: AppDatabase
    val gpsManager = GpsManager(application)

    private val weather = MutableLiveData<WeatherData>()

    val queue = Volley.newRequestQueue(application.applicationContext)

    fun init() {
        if (currentWeather == null) {
            if (!existsWeatherData()) {
                val newWeatherData = createNewWeatherData()
                insertWeatherData(newWeatherData)
                currentWeather = newWeatherData
            } else {
                currentWeather = weatherModel.getWeather()
            }
        }
    }

    private fun createNewWeatherData(): WeatherData {
        val newWeather = WeatherData()
        newWeather.weatherId = 1
        newWeather.feelsLike = 273.15
        newWeather.temperature = 273.15
        return newWeather
    }

    private fun insertWeatherData(weatherData: WeatherData) {
        weatherModel.insertWeatherData(weatherData)
    }

    private fun existsWeatherData(): Boolean {
        return weatherModel.existsWeather()
    }

    init {
        db = AppDatabase.getAppDatabase(application)!!

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val location = gpsManager.getLocation() ?: return
                updateWeather(location.latitude, location.longitude)
            }}, 0, 10000)
    }

    fun updateWeather() {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=Odense,Denmark&appid=254b060232f8bc0ce1f558683ba8d5dc"

        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    Log.d("WEATHER_LOG", "Response: %s".format(response.toString()))

                    val curWeather = WeatherData(
                            response.getJSONArray("weather").getJSONObject(0).getInt("id"),
                            response.getJSONObject("main").getDouble("feels_like"),
                            response.getJSONObject("main").getDouble("temp")
                    )
                    if(existsWeatherData()){
                        db.weatherDataDao().updateWeatherData(curWeather)
                    }else{
                        db.weatherDataDao().insertWeatherData(curWeather)
                    }
                    weather.value = curWeather

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
//                weather.value = WeatherModel(
//                    response.getJSONObject("main").getDouble("temp"),
//                    response.getJSONObject("main").getDouble("feels_like"),
//                    response.getJSONArray("weather").getJSONObject(0).getInt("id")
//                )

                    val curWeather = WeatherData(
                            response.getJSONArray("weather").getJSONObject(0).getInt("id"),
                            response.getJSONObject("main").getDouble("feels_like"),
                            response.getJSONObject("main").getDouble("temp")
                    )
                    if(existsWeatherData())
                    {
                        db.weatherDataDao().updateWeatherData(curWeather)
                    }else{
                        db.weatherDataDao().insertWeatherData(curWeather)
                    }

                    weather.value = curWeather
                },
                { error ->
                    Log.e("WEATHER_LOG", error.toString())
                }
        )

        queue.add(jsonObjectRequest)
    }

    fun getWeather(): LiveData<WeatherData> {
        return weather
    }
}