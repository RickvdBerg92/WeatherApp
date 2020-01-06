package com.example.weatherapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherapp.model.Weather

class WeatherRepository(context: Context) {

    private val weatherDao: WeatherDao

    init {
        val database = WeatherRoomDatabase.getDatabase(context)
        weatherDao = database!!.weatherDao()
    }

    fun getWeather(): LiveData<List<Weather?>?> {
        return weatherDao.getWeather()
    }

    suspend fun insertWeather(weather: Weather) {
        weatherDao.insertWeather(weather)
    }

    suspend fun deleteWeather(weather: Weather) {
        weatherDao.delete(weather)
    }

    suspend fun deleteAllWeather() {
        weatherDao.deleteAllWeather()
    }

}
