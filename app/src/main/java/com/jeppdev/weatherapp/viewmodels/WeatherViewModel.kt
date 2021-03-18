package com.jeppdev.weatherapp.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jeppdev.weatherapp.GpsManager
import com.jeppdev.weatherapp.R
import com.jeppdev.weatherapp.database.WeatherData
import com.jeppdev.weatherapp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder
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
        Log.d("WAPP_WEATHER_LOG", "Initial stored: %s (%s)".format(weather.value?.feelsLike.toString(), weather.value?.weatherId))


        Log.d("WAPP_WEATHER_LOG", "Hello from not coroutine. My thread is: " +
                Thread.currentThread().name)
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("WAPP_WEATHER_LOG", "Hello from coroutine. My thread is: " +
                    Thread.currentThread().name)
            Timer().scheduleAtFixedRate(object : TimerTask() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun run() {
                    val location = gpsManager.getLocation() ?: return
                    val locationName = preferences.getString(resources.getString(R.string.location_name_key), "")
                    val useManualLocation = preferences.getBoolean(resources.getString(R.string.manual_location_key), false) && !locationName.isNullOrEmpty()

                    if (useManualLocation) {
                        if (locationName == null) throw NullPointerException("locationName can't be null!")
                        updateWeather(locationName)
                    } else {
                        updateWeather(location.latitude, location.longitude)
                    }

                }}, 0, 10000)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateWeather(locationName: String) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=${URLEncoder.encode(locationName)}&appid=254b060232f8bc0ce1f558683ba8d5dc"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("WAPP_WEATHER_LOG", "Response: %s".format(response.toString()))

                val weatherData: WeatherData = WeatherData(response.getJSONArray("weather").getJSONObject(0).getInt("id"), response.getJSONObject("main").getDouble("feels_like"))
                weatherData.locationName = "${response.getString("name")}, ${response.getJSONObject("sys").getString("country")}"

                val iconId = response.getJSONArray("weather").getJSONObject(0).getString("icon")
                val iconUrl = "https://openweathermap.org/img/wn/${iconId}@2x.png"

                val iconRequest = ImageRequest(iconUrl,
                    {
                        Log.d("WAPP_WEATHER_LOG", "Got icon!")
                        weatherData.icon = it
                        weather.value = weatherData
                    }, 0, 0, null, Bitmap.Config.RGBA_F16, {
                        Log.e("WAPP_WEATHER_LOG", it.toString())
                    })

                queue.add(iconRequest)

                weather.value = weatherData

                db.weatherDataDao().insertOrUpdate(weatherData)
            },
            { error ->
                Log.e("WAPP_WEATHER_LOG", error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateWeather(latitude: Double, longitude: Double) {
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&appid=254b060232f8bc0ce1f558683ba8d5dc"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("WAPP_WEATHER_LOG", "Response: %s".format(response.toString()))

                val weatherData: WeatherData = WeatherData(response.getJSONArray("weather").getJSONObject(0).getInt("id"), response.getJSONObject("main").getDouble("feels_like"))
                weatherData.locationName = "${response.getString("name")}, ${response.getJSONObject("sys").getString("country")}"

                val iconId = response.getJSONArray("weather").getJSONObject(0).getString("icon")
                val iconUrl = "https://openweathermap.org/img/wn/${iconId}@2x.png"

                val iconRequest = ImageRequest(iconUrl,
                    {
                        Log.d("WAPP_WEATHER_LOG", "Got icon!")
                        weatherData.icon = it
                        weather.value = weatherData
                    }, 0, 0, null, Bitmap.Config.RGBA_F16, {
                        Log.e("WAPP_WEATHER_LOG", it.toString())
                    })

                queue.add(iconRequest)

                weather.value = weatherData

                db.weatherDataDao().insertOrUpdate(weatherData)
            },
            { error ->
                Log.e("WAPP_WEATHER_LOG", error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun getWeather() : LiveData<WeatherData> {
        return weather
    }
}