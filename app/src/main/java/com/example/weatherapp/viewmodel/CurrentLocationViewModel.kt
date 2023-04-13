package com.example.weatherapp.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.api.ApiService
import com.example.weatherapp.model.api.OpenWeatherApiDataClass
import kotlinx.coroutines.launch

class CurrentLocationViewModel() : ViewModel() {
    private val TAG = "CurrentLocationViewModel"
    private val apiService = ApiService()
    val currentWeatherResponse = MutableLiveData<OpenWeatherApiDataClass>()

     fun getCurrentWeather(context: Context){
        if (hasNetwork(context)){
            viewModelScope.launch {
                currentWeatherResponse.value = apiService.getWeather(28.7041, 77.1025).body()
//                Make use of Resource class.
                Log.d(TAG, "getCurrentWeatherrr: ${currentWeatherResponse.value?.main?.toString()}")
            }
        }
        else{
            Log.e(TAG, "getCurrentWeather: No Internet Connection!", )
        }
    }



    private fun hasNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

                else -> false
            }
        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
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