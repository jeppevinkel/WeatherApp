package com.jeppdev.weatherapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import com.jeppdev.weatherapp.R
import com.jeppdev.weatherapp.viewmodels.FuzzyViewModel
import com.jeppdev.weatherapp.viewmodels.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var temperatureTextView: TextView
    private lateinit var feelsLikeTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var fuzzyTemperatureTextView: TextView
    private lateinit var recommendedClothTextView: TextView

    private val weatherViewModel: WeatherViewModel by viewModels()
    private val fuzzyViewModel: FuzzyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        temperatureTextView = findViewById(R.id.temperature_text)
        feelsLikeTextView = findViewById(R.id.feels_like_text)
        locationTextView = findViewById(R.id.location_text)
        fuzzyTemperatureTextView = findViewById(R.id.fuzzy_temperature_text)
        recommendedClothTextView = findViewById(R.id.recommended_clothing_text)

        weatherViewModel.gpsManager.checkPermission(this)

        weatherViewModel.getWeather().observe(this, { weather ->
            feelsLikeTextView.text = "%.2f°C".format(weather.feelsLike - 273.15)
            Log.d("WEATHER_LOG", "Weather changed! (%.2f°C)".format(weather.feelsLike - 273.15))
        })

        //fuzzy test
        weatherViewModel.getWeather().observe(this, { weather ->
            fuzzyTemperatureTextView.text = weather.weatherId.toString()

            recommendedClothTextView.text = fuzzyViewModel.getFuzzyText(weather)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}