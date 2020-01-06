package com.example.weatherapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.database.WeatherRepository
import com.example.weatherapp.model.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherRepository = WeatherRepository(application.applicationContext)

    val liveWeather = weatherRepository.getWeather()

    fun deleteAllWeather() {
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.deleteAllWeather()
        }
    }

    fun deleteWeather(weather: Weather){
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.deleteWeather(weather)
        }
    }

}