package com.jeppdev.weatherapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jeppdev.weatherapp.R

class FuzzyViweModel(application: Application) : AndroidViewModel(application) {
    private val fuzzyWeatherValue = arrayOf(
            application.getString(R.string.frigid),
            application.getString(R.string.freezing),
            application.getString(R.string.cold),
            application.getString(R.string.cool),
            application.getString(R.string.mild),
            application.getString(R.string.warm),
            application.getString(R.string.hot),
            application.getString(R.string.sweltering)
    )
    private val clothing = arrayOf(
            //clothing when frigid
            arrayOf(
                    application.getString(R.string.jacket),
                    application.getString(R.string.mittens),
                    application.getString(R.string.scarf),
                    application.getString(R.string.socks),
                    application.getString(R.string.warm_hat),
                    application.getString(R.string.thick_pants)
            ),
            //clothing when freezing
            arrayOf(
                    application.getString(R.string.jacket),
                    application.getString(R.string.mittens),
                    application.getString(R.string.socks),
                    application.getString(R.string.warm_hat),
                    application.getString(R.string.thick_pants)
            ),
            //clothing when cold
            arrayOf(
                    application.getString(R.string.jacket),
                    application.getString(R.string.socks),
                    application.getString(R.string.warm_hat),
                    application.getString(R.string.jeans)
            ),
            //clothing when cool
            arrayOf(
                    application.getString(R.string.jacket),
                    application.getString(R.string.socks),
                    application.getString(R.string.jeans)
            ),
            //clothing when mild
            arrayOf(
                    application.getString(R.string.jacket),
                    application.getString(R.string.socks),
                    application.getString(R.string.jeans)
            ),
            //clothing when warm
            arrayOf(
                    application.getString(R.string.t_shirt),
                    application.getString(R.string.jeans)
            ),
            //clothing when hot
            arrayOf(
                    application.getString(R.string.t_shirt),
                    application.getString(R.string.shorts)
            ),
            //clothing when sweltering
            arrayOf(
                    application.getString(R.string.t_shirt),
                    application.getString(R.string.jeans)
            )
    )

    /* here begins fuzzy stuff */

    fun getClothing(temperature: Double) : String {
        var returnString = ""

        val arrayOfClothing = clothing[getFuzzyIndex(temperature)]
        val listSize = arrayOfClothing.size
        for (index in 0 .. listSize-3){
            returnString = returnString + arrayOfClothing[index] + ", "
        }
        returnString += arrayOfClothing[listSize-2] + " " + "and"  + " " + arrayOfClothing[listSize-1] //skriver and indtil getString virker
        return returnString

    }

    fun getFuzzyTemperature(temperature: Double) : String{
        return fuzzyWeatherValue[getFuzzyIndex(temperature)]
    }

    private fun getFuzzyIndex(temperature: Double) : Int {
        if (temperature <= -3.25)
            return 0
        else if (-3.25 < temperature && temperature <= 3.13)
            return 1
        else if (3.13 < temperature && temperature <= 7.25)
            return 2
        else if (7.25 < temperature && temperature <= 14.13)
            return 3
        else if (14.13 < temperature && temperature <= 20.75)
            return 4
        else if (20.75 < temperature && temperature <= 26.5)
            return 5
        else if (26.5 < temperature && temperature <= 32)
            return 6
        else if (32 < temperature)
            return 7
        else return -1
    }

}