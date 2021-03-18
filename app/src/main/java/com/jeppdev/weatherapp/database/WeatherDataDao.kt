package com.jeppdev.weatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeatherDataDao{
    @Query("SELECT EXISTS(SELECT * FROM weatherdata where id = 1)")
    fun exists(): Boolean

    @Query("SELECT COUNT(*) from weatherdata")
    fun countWeather(): Int

    @Query("SELECT * FROM weatherdata where id = 1")
    fun getWeatherData(): WeatherData

    @Update
    fun updateWeatherData(weatherData: WeatherData): Int

    @Insert
    fun insertWeatherData(weatherData: WeatherData)

}