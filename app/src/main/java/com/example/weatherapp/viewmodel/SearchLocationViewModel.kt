package com.example.weatherapp.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.api.ApiService
import com.example.weatherapp.model.api.OpenWeatherApiDataClass
import com.example.weatherapp.model.api.SearchLocationResultDataClass
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.utils.Variables
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchLocationViewModel : ViewModel() {
    private val TAG = "SearchLocationViewModel"
    private val apiService = ApiService()
    val weatherResponse = MutableLiveData<Resource<OpenWeatherApiDataClass>>()
    val coordinatesResponse = MutableLiveData<Resource<SearchLocationResultDataClass>>()
    var latitude = 200.0
    var longitude = 200.0


    fun getCoordinates(cityName: String, context: Context) {
        coordinatesResponse.postValue(Resource.Loading())

        if (hasNetwork(context)) {
            viewModelScope.launch {
                val response = apiService.getCoordinates(cityName)
                coordinatesResponse.postValue(handleCoordinatesResponse(response))
            }
        } else {
            coordinatesResponse.postValue(Resource.Error("No Internet!"))
        }

        coordinatesResponse.observe(context as LifecycleOwner) { resource ->
            Log.d(TAG, "coordinatesResponse changed: ${resource.data}")
            if (resource?.data != null && resource.data.isNotEmpty()) {
                latitude = resource.data[0].lat
                longitude = resource.data[0].lon

                getCurrentWeather(context)
            }
        }
    }


    private fun handleCoordinatesResponse(response: Response<SearchLocationResultDataClass>): Resource<SearchLocationResultDataClass> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }


    private fun getCurrentWeather(context: Context) {
        weatherResponse.postValue(Resource.Loading())

        if (hasNetwork(context)) {
            viewModelScope.launch {
                val response = apiService.getWeather(latitude, longitude);
                Log.d(TAG, "Response -> ${response.body()}")
                weatherResponse.postValue(handleWeatherResponse(response))
                Log.d(TAG, "getCurrentWeather from search: ${weatherResponse.value?.data}")
            }
        } else {
            Log.e(TAG, "getCurrentWeather: No Internet Connection!")
        }

        weatherResponse.observe(context as LifecycleOwner){ result ->
            Log.d(TAG, "coordinatesResponse changed: ${result.data}")
        }
    }


    private fun handleWeatherResponse(response: Response<OpenWeatherApiDataClass>): Resource<OpenWeatherApiDataClass> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }


    private fun hasNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true

                    else -> false
                }
            }
        }

        return false
    }

}