package com.example.weatherapp.application

import android.app.Application
import com.example.weatherapp.model.database.WeatherRepository


class WeatherApplication : Application(){
    private val database by lazy{
//        ArticleDatabase.getDatabase(this@WeatherApplication)
    }
    val repository by lazy {
        WeatherRepository()
    }
}