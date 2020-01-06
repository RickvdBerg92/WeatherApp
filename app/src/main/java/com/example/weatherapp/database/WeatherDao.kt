package com.example.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.model.Weather

@Dao
interface WeatherDao {

    @Insert
    suspend fun insertWeather(weather: Weather)

    @Query("SELECT * FROM Weather")
    fun getWeather(): LiveData<List<Weather?>?>

    @Delete
    suspend fun delete(weather: Weather)

    @Query("DELETE FROM Weather")
    suspend fun deleteAllWeather()

}
