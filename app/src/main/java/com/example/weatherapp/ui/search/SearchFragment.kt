package com.example.weatherapp.ui.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.weather_card.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView


class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var weatherAdapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        searchViewModel =
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        weatherAdapter = WeatherAdapter(searchViewModel.weatherList) { saveCallback() }
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvSearchResults.layoutManager = LinearLayoutManager(context)
        rvSearchResults.adapter = weatherAdapter
        svLocation.isIconified = false
        svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(s: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(s: String): Boolean {
                searchLocation()
                return true
            }
        })
    }

    fun searchLocation() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        searchViewModel.findWeather(svLocation.query)
        weatherAdapter.notifyDataSetChanged()

    }

    private fun saveCallback() {
        val item =
            Weather(
                tvCity.text.toString(),
                tvTemp.text.toString(),
                tvType.text.toString(),
                tvDescription.text.toString(),
                searchViewModel.weatherList[0].icon,
                null
            )
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                searchViewModel.weatherRepository.insertWeather(item)
            }
        }
    }
}