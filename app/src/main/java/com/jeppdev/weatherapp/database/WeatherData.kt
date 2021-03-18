package com.jeppdev.weatherapp.database

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "WeatherData")
class WeatherData {
    @PrimaryKey
    var id = 1
    var weatherId = 0
    var feelsLike: Double = 0.0
    var locationName: String = ""
    @Ignore
    var icon: Bitmap? = null

    constructor(){}

    constructor(_weatherId: Int, _feelsLike: Double, _locationName: String = "") {
        weatherId = _weatherId
        feelsLike = _feelsLike
        locationName = _locationName
    }

    constructor(_weatherId: Int, _feelsLike: Double, _locationName: String, _icon: Bitmap) {
        weatherId = _weatherId
        feelsLike = _feelsLike
        locationName = _locationName
        icon = _icon
    }

}