package com.jeppdev.weatherapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import com.jeppdev.weatherapp.R
import com.jeppdev.weatherapp.viewmodels.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var feelsLikeTextView: TextView
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        temperatureTextView = findViewById(R.id.temperature_text)
        feelsLikeTextView = findViewById(R.id.feels_like_text)

       // temperatureTextView.text = "%.2f°C".format(weatherViewModel.getWeather()!!.feelsLike - 273.15)
        weatherViewModel.getWeather().observe(this, { weather ->
            temperatureTextView.text = "%.2f°C".format(weather.temperature - 273.15)
            feelsLikeTextView.text = "%.2f°C".format(weather.feelsLike - 273.15)
            Log.d("WEATHER_LOG", "Weather changed!")
        })
        weatherViewModel.updateWeather()
    }
}