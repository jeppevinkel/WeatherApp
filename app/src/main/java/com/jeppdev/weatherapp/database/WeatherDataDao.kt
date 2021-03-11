package com.jeppdev.weatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeatherDataDao{
    @Query("SELECT EXISTS(SELECT * FROM weatherdata where id = 1)")
    fun exists(): Boolean

    @Query("SELECT * FROM weatherdata where id = 1")
    fun getWeatherData(): WeatherData

    @Update
    fun updateWeatherData(weatherData: WeatherData): Int

    @Insert
    fun insertWeatherData(weatherData: WeatherData)

    fun insertOrUpdate(weatherData: WeatherData){
        if (exists()){
            updateWeatherData(weatherData)
        }else {
            insertWeatherData(weatherData)
        }
    }

    fun getOrCreate(): WeatherData{
        return if(exists()){
            getWeatherData()
        } else {
            WeatherData(0, 0.0)
        }
    }

}