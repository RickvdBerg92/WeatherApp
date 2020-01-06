package com.example.weatherapp.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.weather_card.view.*
import java.net.URL


class WeatherAdapter(private val weather: List<Weather>, val clickListener: () -> Unit) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var toggle: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.weather_card,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return weather.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weather[position])

    }

    inner class iconTask(iconId: String) : AsyncTask<String, Void, Bitmap>(){
        private val icon = iconId

        override fun doInBackground(vararg params: String?): Bitmap? {
            return try {
                val url = URL("http://openweathermap.org/img/w/" + icon + ".png")
                val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                bmp
            } catch(e: Exception) {
                null
            }
        }
    }

    fun toggleFab(activate: Boolean){
        toggle = activate
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) {
            itemView.tvCity.text = weather.location
            itemView.tvTemp.text = weather.temp
            itemView.tvType.text = weather.type
            itemView.tvDescription.text = weather.description
            itemView.ivIcon.setImageBitmap(iconTask(weather.icon).execute().get())
            if(toggle){
                itemView.fabSave.hide()
            }
            itemView.fabSave.setOnClickListener { clickListener() }
        }
    }
}
