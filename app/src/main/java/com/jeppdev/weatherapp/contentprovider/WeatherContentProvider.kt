package com.jeppdev.weatherapp.contentprovider

import android.content.ContentProvider
import android.content.ContentUris.withAppendedId
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.room.Room
import com.jeppdev.weatherapp.database.AppDatabase
import com.jeppdev.weatherapp.database.WeatherDataDao

class WeatherContentProvider : ContentProvider() {
    val PROVIDER_NAME = "com.jeppdev.weatherapp.provider"
    val PROVIDER_URL = "content://${PROVIDER_NAME}/weather"
    private lateinit var db: AppDatabase
    private var weatherDao: WeatherDataDao? = null

    override fun onCreate(): Boolean {
        db = context?.let { AppDatabase.getAppDatabase(it) }!!
        weatherDao = db.weatherDataDao()

        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        var localSortOrder: String = sortOrder ?: ""
        var localSelection: String = selection ?: ""

        TODO("Implement onCreate")
    }

    override fun getType(uri: Uri): String? {
        TODO("Implement getType")
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        Log.d("WAPP_CONTENT_PROVIDER", contentValues.toString())

        return withAppendedId(uri, 1)
        TODO("Implement insert")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Implement delete")
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Implement update")
    }
}