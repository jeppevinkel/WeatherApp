package com.jeppdev.weatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WeatherData")
class WeatherData {
    @JvmField
    @PrimaryKey
    var id = 1

    @JvmField
    var weatherId = 1

    @JvmField
    var feelsLike: Double = 0.0

    @JvmField
    var temperature: Double = 0.0

    constructor(_weatherId: Int, _feelsLike: Double, _temp: Double) {
        weatherId = _weatherId
        feelsLike = _feelsLike
        temperature = _temp
    }
    constructor(){}
}
