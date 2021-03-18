package com.jeppdev.weatherapp.models

import com.jeppdev.weatherapp.database.SettingsDao
import com.jeppdev.weatherapp.database.Settings
import com.jeppdev.weatherapp.database.WeatherData
import com.jeppdev.weatherapp.database.WeatherDataDao

class WeatherModel(var temperature: Double, var feelsLike: Double, var id: Int) {
}

//class WeatherModel(private val settingsDao: SettingsDao, private val weatherDataDao: WeatherDataDao) {
//
//    //Get functions
//    fun getSettings(): Settings {
//        return settingsDao.getOrCreate()
//    }
//
//    fun getWeather(): WeatherData {
//        return weatherDataDao.getOrCreate()
//    }
//
//    //Update functions
//    fun updateSettings(settings: Settings){
//        settingsDao.insertOrUpdate(settings)
//    }
//
//    fun updateWeather(weatherData: WeatherData){
//        weatherDataDao.insertOrUpdate(weatherData)
//    }
//}