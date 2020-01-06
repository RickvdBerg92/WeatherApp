package com.example.weatherapp.ui.search

import android.app.Application
import android.os.AsyncTask
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.weatherapp.database.WeatherRepository
import com.example.weatherapp.model.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.FileNotFoundException
import java.lang.Exception
import java.net.URL

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    val weatherRepository = WeatherRepository(application.applicationContext)
    var weatherList = arrayListOf<Weather>()
    val key: String = "d94018d20142c809f51fd2e9ff0ab776"

    fun findWeather(location: CharSequence) {
        weatherTask(location, false).execute()
    }

    inner class weatherTask(private val city: CharSequence, private val save: Boolean) :
        AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {
            val response: String?
            return try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$key").readText()
                response
            } catch (e: Exception) {
                null
            }
        }

        override fun onPostExecute(result: String?) {
            if (result.isNullOrEmpty()) {
                Toast.makeText(getApplication(), "Thats not a valid city name", Toast.LENGTH_LONG).show()
                return
            }
            super.onPostExecute(result)
            val json = JSONObject(result)
            val item = Weather(
                json.getString("name"),
                json.getJSONObject("main").getString("temp"),
                json.getJSONArray("weather").getJSONObject(0).getString("main"),
                json.getJSONArray("weather").getJSONObject(0).getString("description"),
                json.getJSONArray("weather").getJSONObject(0).getString("icon"),
                null
            )
            weatherList.clear()
            weatherList.add(item)
            if (save) {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        weatherRepository.insertWeather(item)
                    }
                }
            }
        }

    }
}

