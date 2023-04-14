package com.example.weatherapp.model.api

import com.example.weatherapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherInterface {
    @GET(Constants.ENDPOINT_FOR_CURRENT_LOCATION)
    suspend fun getWeather(
        @Query(Constants.LATITUDE) lat : Double,
        @Query(Constants.LONGITUDE) lon : Double,
        @Query(Constants.API_KEY) appid: String
    ) : Response<OpenWeatherApiDataClass>


    @GET(Constants.ENDPOINT_FOR_SEARCH_LOCATION)
    suspend fun getCoordinates(
        @Query("q") cityName: String,
        @Query("appid") appid: String
    ) : Response<SearchLocationResultDataClass>
}