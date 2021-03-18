package com.jeppdev.weatherapp.models

import com.jeppdev.weatherapp.database.Settings
import com.jeppdev.weatherapp.database.SettingsDao

class SettingsModel(private val settingsDao: SettingsDao){

    /*Exists function*/
    fun existsSettings() : Boolean{
        return settingsDao.exists()
    }

    /*Get function*/
    fun getSettings(): Settings {
        return settingsDao.getSettings()
    }

    /*Insert function*/
    fun insertSettings(settings: Settings){
        settingsDao.insertSettings(settings)
    }

    /*Update function*/
    fun updateSettings(settings: Settings){
        settingsDao.updateSettings(settings)
    }
}