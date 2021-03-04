package com.jeppdev.weatherapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jeppdev.weatherapp.database.WeatherData
import com.jeppdev.weatherapp.R
import com.jeppdev.weatherapp.database.AppDatabase
import com.jeppdev.weatherapp.models.WeatherModel

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    //lateinit var model: WeatherModel
    //var currentWeather: WeatherData? = null

    private lateinit var db: AppDatabase

    private val weather = MutableLiveData<WeatherData>()
    private val fuzzyWeatherValue = arrayOf(
        application.getString(R.string.frigid),
        application.getString(R.string.freezing),
        application.getString(R.string.cold),
        application.getString(R.string.cool),
        application.getString(R.string.mild),
        application.getString(R.string.warm),
        application.getString(R.string.hot),
        application.getString(R.string.sweltering)
    )
    private val clothing = arrayOf(
        //clothing when frigid
        arrayOf(
            application.getString(R.string.jacket),
            application.getString(R.string.mittens),
            application.getString(R.string.scarf),
            application.getString(R.string.socks),
            application.getString(R.string.warm_hat),
            application.getString(R.string.thick_pants)
        ),
        //clothing when freezing
        arrayOf(
            application.getString(R.string.jacket),
            application.getString(R.string.mittens),
            application.getString(R.string.socks),
            application.getString(R.string.warm_hat),
            application.getString(R.string.thick_pants)
        ),
        //clothing when cold
        arrayOf(
            application.getString(R.string.jacket),
            application.getString(R.string.socks),
            application.getString(R.string.warm_hat),
            application.getString(R.string.jeans)
        ),
        //clothing when cool
        arrayOf(
            application.getString(R.string.jacket),
            application.getString(R.string.socks),
            application.getString(R.string.jeans)
        ),
        //clothing when mild
        arrayOf(
            application.getString(R.string.jacket),
            application.getString(R.string.socks),
            application.getString(R.string.jeans)
        ),
        //clothing when warm
        arrayOf(
            application.getString(R.string.t_shirt),
            application.getString(R.string.jeans)
        ),
        //clothing when hot
        arrayOf(
            application.getString(R.string.t_shirt),
            application.getString(R.string.shorts)
        ),
        //clothing when sweltering
        arrayOf(
            application.getString(R.string.t_shirt),
            application.getString(R.string.jeans)
        )
    )


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

    /* here begins fuzzy stuff */

    fun getClothing(temperature: Double) : String {
        var returnString = ""

        val arrayOfClothing = clothing[getFuzzyIndex(temperature)]
        val listSize = arrayOfClothing.size
        for (index in 0 .. listSize-3){
            returnString = returnString + arrayOfClothing[index] + ", "
        }
        returnString += arrayOfClothing[listSize-2] + " " + "and"  + " " + arrayOfClothing[listSize-1] //skriver and indtil getString virker
        return returnString

    }

    fun getFuzzyTemperature(temperature: Double) : String{
        return fuzzyWeatherValue[getFuzzyIndex(temperature)]
    }

    private fun getFuzzyIndex(temperature: Double) : Int {
        if (temperature <= -3.25)
            return 0
        else if (-3.25 < temperature && temperature <= 3.13)
            return 1
        else if (3.13 < temperature && temperature <= 7.25)
            return 2
        else if (7.25 < temperature && temperature <= 14.13)
            return 3
        else if (14.13 < temperature && temperature <= 20.75)
            return 4
        else if (20.75 < temperature && temperature <= 26.5)
            return 5
        else if (26.5 < temperature && temperature <= 32)
            return 6
        else if (32 < temperature)
            return 7
        else return -1
    }
}