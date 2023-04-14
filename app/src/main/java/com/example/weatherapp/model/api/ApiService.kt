package com.example.weatherapp.model.api

import com.example.weatherapp.utils.Constants
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    private val api = Retrofit.Builder()
        .baseUrl(Constants.URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(OpenWeatherInterface::class.java)


    suspend fun getWeather(lat: Double, long : Double): Response<OpenWeatherApiDataClass> {
        return api.getWeather(lat, long, "25bfdcbc3980c901a4c75d4b6656426b")
    }

    suspend fun getCoordinates(cityName: String) : Response<SearchLocationResultDataClass>{
        return api.getCoordinates(cityName, "25bfdcbc3980c901a4c75d4b6656426b")
    }
}