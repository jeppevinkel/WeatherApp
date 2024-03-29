package com.jeppdev.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WeatherData::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDataDao(): WeatherDataDao
    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase? {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "weather-database")
                    .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }
    }
}