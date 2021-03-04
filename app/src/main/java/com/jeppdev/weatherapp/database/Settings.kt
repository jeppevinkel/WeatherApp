package com.jeppdev.weatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.material.internal.ManufacturerUtils

@Entity(tableName = "Settings")
class Settings {
    @PrimaryKey
    var settingsId = 0
    //placering (coordinates)
    var lon: Double = 0.0
    var lat: Double = 0.0

    //For at bruge koordinater eller den by man skriver end
    var useManualLocation: Boolean = false

    var cityName: String ?= null

    constructor(){}

    constructor(_lon: Double, _lat: Double, _useManualLocation: Boolean, _cityName: String?){
        lon = _lon
        lat = _lat
        useManualLocation = _useManualLocation
        cityName = _cityName
    }
}