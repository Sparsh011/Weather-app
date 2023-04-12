package com.example.weatherapp.model.api

import com.example.weatherapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherInterface {
    @GET(Constants.ENDPOINT)
    suspend fun getWeather(
        @Query(Constants.LATITUDE) lat : Double,
        @Query(Constants.LONGITUDE) lon : Double,
        @Query(Constants.API_KEY) appid: String
    ) : Response<OpenWeatherApiDataClass>
}