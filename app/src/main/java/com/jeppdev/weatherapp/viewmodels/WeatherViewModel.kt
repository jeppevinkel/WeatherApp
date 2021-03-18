package com.jeppdev.weatherapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jeppdev.weatherapp.GpsManager
import com.jeppdev.weatherapp.R
import com.jeppdev.weatherapp.database.WeatherData
import com.jeppdev.weatherapp.database.AppDatabase
import org.json.JSONObject
import java.util.*

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private var db: AppDatabase
    val gpsManager = GpsManager(application)
    val preferences = PreferenceManager.getDefaultSharedPreferences(application)
    val resources = application.resources

    private val weather = MutableLiveData<WeatherData>()

    val queue = Volley.newRequestQueue(application.applicationContext)

    init {
        db = AppDatabase.getAppDatabase(application)!!

        weather.value = db.weatherDataDao().getOrCreate()
        Log.d("WEATHER_LOG", "Initial stored: %s (%s)".format(weather.value?.feelsLike.toString(), weather.value?.weatherId))

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val location = gpsManager.getLocation() ?: return
                val locationName = preferences.getString(resources.getString(R.string.location_name_key), "")
                val useManualLocation = preferences.getBoolean(resources.getString(R.string.manual_location_key), false) && !locationName.isNullOrEmpty()
//                updateWeather(location.latitude, location.longitude)

                if (useManualLocation) {
                    if (locationName == null) throw NullPointerException("locationName can't be null!")
//                    Log.d("WEATHER_LOG", "Manual Location Update")
                    updateWeather(locationName)
                } else {
//                    Log.d("WEATHER_LOG", "GPS Location Update")
                    updateWeather(location.latitude, location.longitude)
                }

            }}, 0, 10000)
    }

    fun updateWeather(locationName: String) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=${locationName}&appid=254b060232f8bc0ce1f558683ba8d5dc"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("WEATHER_LOG", "Response: %s".format(response.toString()))

                val weatherData: WeatherData = WeatherData(response.getJSONArray("weather").getJSONObject(0).getInt("id"), response.getJSONObject("main").getDouble("feels_like"))

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

                val weatherData: WeatherData = WeatherData(response.getJSONArray("weather").getJSONObject(0).getInt("id"), response.getJSONObject("main").getDouble("feels_like"))

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