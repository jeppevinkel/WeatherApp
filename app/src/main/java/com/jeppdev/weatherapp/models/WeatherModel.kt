package com.jeppdev.weatherapp.models

import com.jeppdev.weatherapp.database.SettingsDao
import com.jeppdev.weatherapp.database.Settings
import com.jeppdev.weatherapp.database.WeatherData
import com.jeppdev.weatherapp.database.WeatherDataDao

//class WeatherModel(var temperature: Double, var feelsLike: Double, var id: Int) {
//}

class WeatherModel(private val weatherDataDao: WeatherDataDao) {


    //Exists function
    fun existsWeather() : Boolean{
        return weatherDataDao.exists()
    }

    fun countWeather(): Int {
        return weatherDataDao.countWeather()
    }

    //Get function
    fun getWeather() : WeatherData {
        return weatherDataDao.getWeatherData()
    }

    //Insert function
    fun insertWeatherData(weatherData: WeatherData){
        weatherDataDao.insertWeatherData(weatherData)
    }

    //Update function
    fun updateWeather(weatherData: WeatherData){
        weatherDataDao.updateWeatherData(weatherData)
    }
}

