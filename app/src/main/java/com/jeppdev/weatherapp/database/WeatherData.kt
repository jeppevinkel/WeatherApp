package com.jeppdev.weatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WeatherData")
class WeatherData {
    @PrimaryKey
    var id = 1
    var weatherId = 1
    var feelsLike: Double = 0.0

    constructor(){}

    constructor(_weatherId: Int, _feelsLike: Double) {
        weatherId = _weatherId
        feelsLike = _feelsLike
    }

}