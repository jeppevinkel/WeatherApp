package com.jeppdev.weatherapp.database

import androidx.room.*

@Dao
interface SettingsDao{
    @Query("SELECT EXISTS(SELECT * FROM settings where settingsId = 1)")
    fun exists(): Boolean

    @Query("SELECT * FROM Settings where settingsId = 1")
    fun getSettings(): Settings

    @Update
    fun updateSettings(settings: Settings): Int

    @Insert
    fun insertSettings(settings: Settings)

//    fun insertOrUpdate(settings: Settings) {
//        if(exists()){
//            updateSettings(settings)
//        } else {
//            insertSettings(settings)
//        }
//    }
//
//    fun getOrCreate(): Settings {
//        return if(exists()) {
//            getSettings()
//        } else {
//
//            Settings(0.0, 0.0, false, null)
//        }
//    }

}

