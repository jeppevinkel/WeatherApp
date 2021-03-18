package com.jeppdev.weatherapp.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class WeatherContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        TODO("Implement onCreate")
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        TODO("Implement query")
    }

    override fun getType(uri: Uri): String? {
        TODO("Implement getType")
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        TODO("Implement insert")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Implement delete")
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Implement update")
    }
}