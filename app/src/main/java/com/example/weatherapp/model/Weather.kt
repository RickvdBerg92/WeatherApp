package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(

    val location: String,

    val temp: String,

    val type: String,

    val description: String,

    val icon: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int?
)