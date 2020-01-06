package com.example.weatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherAdapter
import com.example.weatherapp.ui.search.SearchViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val weather = arrayListOf<Weather>()
    private val weatherAdapter = WeatherAdapter(weather) {}
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.liveWeather.observe(this, Observer { list ->
            if (list != null) {
                weather.clear()
                list.forEach { it?.let { it1 -> weather.add(it1) } }
                weatherAdapter.notifyDataSetChanged()
            }
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvCities.layoutManager = LinearLayoutManager(context)
        rvCities.adapter = weatherAdapter
        createItemTouchHelper().attachToRecyclerView(rvCities)
        weatherAdapter.toggleFab(true)
        fabRefresh.setOnClickListener { refreshWeather() }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                homeViewModel.deleteWeather(weather[position])
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun refreshWeather(){
        homeViewModel.deleteAllWeather()
        weather.forEach{
            SearchViewModel(activity!!.application).weatherTask(it.location, true).execute()
        }
    }
}