package com.jeppdev.weatherapp

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.jeppdev.weatherapp.database.AppDatabase
import com.jeppdev.weatherapp.database.WeatherDataDao

const val PROVIDER_NAME = "com.jeppdev.weatherapp.provider"
const val PROVIDER_URL = "content://${PROVIDER_NAME}/weather"

class WeatherContentProviderTest : ContentProvider() {
    private lateinit var db: AppDatabase
    private var weatherDao: WeatherDataDao? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO("Implement this to handle requests for the MIME type of the data" +
                "at the given URI")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
//        Log.d("WAPP_CONTENT_PROVIDER", values.toString())

        return uri
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun onCreate(): Boolean {
        Log.d("WAPP_CONTENT_PROVIDER", "Content provider created.")

        db = context?.let { AppDatabase.getAppDatabase(it) }!!
        weatherDao = db.weatherDataDao()

        return true
        TODO("Implement this to initialize your content provider on startup.")
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        Log.d("WAPP_CONTENT_PROVIDER", "Returning weather data...")
        val weather = weatherDao!!.getOrCreate()

        val c = MatrixCursor(arrayOf("id", "weatherId", "feelsLike"))
        c.addRow(arrayOf(weather.id, weather.weatherId, weather.feelsLike))

        return c
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

    companion object {
        val CONTENT_URI = Uri.parse(com.jeppdev.weatherapp.contentprovider.PROVIDER_URL)
    }
}