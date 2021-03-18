package com.jeppdev.weatherapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import com.jeppdev.weatherapp.R
import com.jeppdev.weatherapp.database.WeatherData
import java.lang.StringBuilder

class FuzzyViewModel(application: Application) : AndroidViewModel(application) {

    val preferences = PreferenceManager.getDefaultSharedPreferences(application)
    val resources = application.resources

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
                    application.getString(R.string.shorts)
            )
    )

    //weather id https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
    private val sunnyConditions = arrayOf(
            800, //Clear sky
            801 //Few clouds
    )

    //weather id https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
    private val rainingConditions = arrayOf(
//            300, // light intensity drizzle
//            301, // drizzle
//            302, // heavy intensity drizzle
            310, // light intensity drizzle rain
            311, // drizzle rain
            312, // heavy intensity drizzle rain
            302, // shower rain and drizzle
            310, // heavy shower rain and drizzle
            311, // shower drizzle

            500, // light rain
            501, // moderate rain
            502, // heavy intensity rain
            503, // very heavy rain
            504, // extreme rain
            511, // freezing rain
            520, // light intensity shower rain
            521, // shower rain
            522, // heavy intensity shower rain
            531 // ragged shower rain
    )

    //weather id https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
    private val hazardousWeather = arrayOf(
            781, // Tornado
            771, // Squall
            762 // volcanic ash recommended procedure https://www.weather.gov/safety/airquality-volcanic-ash
    )

    /*********************************************************************************************
     ***************************** here begins fuzzy calculations ********************************
     ********************************************************************************************/

    fun getFuzzyText(weatherData: WeatherData) : String
    {
        val fuzzyText = StringBuilder()
        var iCanGoOutside = true

        for (i in hazardousWeather)
            iCanGoOutside = iCanGoOutside && i != weatherData.weatherId


        if (iCanGoOutside) {
            fuzzyText.append(getApplication<Application>().getString(R.string.str_temperature_introduction) + ' ')

            // get weather temperature
            val fuzzyTemperature = fuzzyWeatherValue[getFuzzyIndex(weatherData.feelsLike - 273.15)]
            fuzzyText.append("$fuzzyTemperature. ")

            //get clothing
            fuzzyText.append(getApplication<Application>().getString(R.string.consider).capitalize() + ' ')
            fuzzyText.appendLine(getClothing(weatherData.feelsLike - 273.15) + '.')


            //get accessories
            var raining = false
            var sunny = false
            for (i in sunnyConditions)
                sunny = sunny || (i == weatherData.weatherId)
            for (i in rainingConditions)
                raining = raining || (i == weatherData.weatherId)

            if (raining)
                fuzzyText.appendLine(getApplication<Application>().getString(R.string.str_raining_introduction))
            if (sunny)
                fuzzyText.appendLine(getApplication<Application>().getString(R.string.str_sunny_introduction))



        }
        else
            fuzzyText.append("It is recommended not to go outside.")


        return fuzzyText.toString()
    }

    private fun getFuzzyIndex(temperature: Double) : Int {

        val sensitivity = preferences.getString(resources.getString(R.string.temperature_sensitivity_key), "cold_resistant")
        when(sensitivity){
            "heat_resistant" ->{
                if (temperature <= -3.25)//frigid
                    return 0
                else if (-3.25 < temperature && temperature <= 3.13) //freezing
                    return 1
                else if (3.13 < temperature && temperature <= 7.25) //cold
                    return 2
                else if (7.25 < temperature && temperature <= 14.13) //cool
                    return 3
                else if (14.13 < temperature && temperature <= 20.75) //mild
                    return 4
                else if (20.75 < temperature && temperature <= 26.5) //warm
                    return 5
                else if (26.5 < temperature && temperature <= 32) //hot
                    return 6
                else if (32 < temperature) //sweltering
                    return 7
                else
                    return -1 //error should never reach this point
            }
            "cold_resistant" ->{
                if (temperature <= -6)//frigid
                    return 0
                else if (-6 < temperature && temperature <= -0.25) //freezing
                    return 1
                else if (0.25 < temperature && temperature <= 8.38) //cold
                    return 2
                else if (8.38 < temperature && temperature <= 16.75) //cool
                    return 3
                else if (16.75 < temperature && temperature <= 22.75) //mild
                    return 4
                else if (22.75 < temperature && temperature <= 38.75) //warm
                    return 5
                else if (38.75 < temperature && temperature <= 34.75) //hot
                    return 6
                else if (34.75 < temperature) //sweltering
                    return 7
                else
                    return -1 //error should never reach this point
            }
            "viking" ->{
                return 6 // always hot when you are a viking
            }
        }
        return -1 //error should never reach this point
    }

    private fun getClothing(temperature: Double) : String {
        var returnString = ""
        val andString: String = getApplication<Application>().getString(R.string.and)
        val arrayOfClothing = clothing[getFuzzyIndex(temperature)]
        val listSize = arrayOfClothing.size
        for (index in 0 .. listSize-3){
            returnString = returnString + arrayOfClothing[index] + ", "
        }
        returnString += arrayOfClothing[listSize-2] + " " + andString + " " + arrayOfClothing[listSize-1]
        return returnString

    }

    fun getFuzzyTemperature(temperature: Double) : String{
        return fuzzyWeatherValue[getFuzzyIndex(temperature)]
    }


}