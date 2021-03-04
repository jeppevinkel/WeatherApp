package com.jeppdev.weatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WeatherData")
class WeatherData {
    @PrimaryKey
    var weatherId = 1
    var feelsLike: Double = 0.0

    constructor(){}

    constructor(_feelsLike: Double) {
        feelsLike = _feelsLike
    }

}