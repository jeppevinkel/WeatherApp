package com.jeppdev.weatherapp.views

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.jeppdev.weatherapp.R
import com.jeppdev.weatherapp.contentprovider.PROVIDER_NAME
import com.jeppdev.weatherapp.contentprovider.PROVIDER_URL
import com.jeppdev.weatherapp.contentprovider.WeatherContentProvider
import com.jeppdev.weatherapp.viewmodels.FuzzyViewModel
import com.jeppdev.weatherapp.viewmodels.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var recommendedClothTextView: TextView
    private lateinit var weatherIconImageView: ImageView
    private lateinit var weatherLocationNameTextView: TextView

    private val weatherViewModel: WeatherViewModel by viewModels()
    private val fuzzyViewModel: FuzzyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recommendedClothTextView = findViewById(R.id.recommended_clothing_text)
        weatherIconImageView = findViewById(R.id.imageWeatherIcon)
        weatherLocationNameTextView = findViewById(R.id.location_name_text)

        weatherViewModel.gpsManager.checkPermission(this)

        val cursor = contentResolver.query(WeatherContentProvider.CONTENT_URI, null, null, null, null)

        if (cursor!!.moveToFirst()) {
            val strBuild = StringBuilder()
            while (!cursor.isAfterLast) {
                strBuild.append("${cursor.getString(cursor.getColumnIndex("id"))}-${cursor.getString(cursor.getColumnIndex("weatherId"))}-${cursor.getString(cursor.getColumnIndex("feelsLike"))}")
                cursor.moveToNext()
            }
            Log.d("WAPP_CONTENT_PROVIDER", strBuild.toString())
        }
        else {
            Log.d("WAPP_CONTENT_PROVIDER", "No data found.")
        }
        cursor.close()

        weatherViewModel.getWeather().observe(this, { weather ->
            Log.d("WAPP_WEATHER_LOG", "Weather changed! (%.2f°C), (%s), (%s)".format(weather.feelsLike - 273.15, weather.weatherId, weather.locationName))
        })

        //fuzzy test
        weatherViewModel.getWeather().observe(this, { weather ->

            recommendedClothTextView.text = fuzzyViewModel.getFuzzyText(weather)

            weatherLocationNameTextView.text = weather.locationName

            if (weather.icon != null) {
                weatherIconImageView.setImageBitmap(weather.icon)
            }
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