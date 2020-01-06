package com.example.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.model.Weather

@Database(entities = [Weather::class], version = 1, exportSchema = false)
abstract class WeatherRoomDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    companion object {
        private const val DATABASE_NAME = "WEATHER"

        @Volatile
        private var INSTANCE: WeatherRoomDatabase? = null

        fun getDatabase(context: Context): WeatherRoomDatabase? {
            if (INSTANCE != null) {
                return INSTANCE
            }
            synchronized(WeatherRoomDatabase::class.java) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherRoomDatabase::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE
        }
    }

}
