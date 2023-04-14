package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CurrentLocationFragmentBinding
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.utils.Variables
import com.example.weatherapp.viewmodel.CurrentLocationViewModel

class CurrentLocationFragment : Fragment(R.layout.current_location_fragment) {
    private var currentLocationFragmentBinding : CurrentLocationFragmentBinding? = null
    private var currentLocationViewModel: CurrentLocationViewModel? = null
    private val TAG = "CurrentLocationFragment"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentLocationFragmentBinding = CurrentLocationFragmentBinding.inflate(inflater, container, false)
        return currentLocationFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentLocationViewModel = ViewModelProvider(this)[CurrentLocationViewModel::class.java]


        context?.let {
            currentLocationViewModel?.getCurrentWeather(it)
        }


        viewModelObserver()
    }


    private fun viewModelObserver(){
        currentLocationViewModel?.currentWeatherResponse?.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    Log.i(TAG, "viewModelObserverrr: ${response.data}")
                    currentLocationFragmentBinding?.pbCurrentLocation?.visibility = View.GONE
                    var temp = response.data?.main?.temp
                    temp = temp?.minus(273)
                    temp = temp?.toInt()?.toDouble()

                    currentLocationFragmentBinding?.tvCurrentCityWeather?.text = temp.toString()
                    currentLocationFragmentBinding?.tvCurrentCityWeather?.visibility = View.VISIBLE
                    currentLocationFragmentBinding?.tvHeading?.text =
                        "${Variables.CITY_NAME}, ${Variables.STATE_NAME}, ${Variables.COUNTRY_NAME}"


                    if (temp != null) {
                        if (temp > 30){
                            currentLocationFragmentBinding?.tvEmoji?.text = "\uD83E\uDD75"
                        }
                        else if (temp > 10 && temp < 30){
                            currentLocationFragmentBinding?.tvEmoji?.text = "\uD83D\uDE04"
                        }
                        else{
                            currentLocationFragmentBinding?.tvEmoji?.text = "\uD83E\uDD76\n"
                        }

                        currentLocationFragmentBinding?.tvEmoji?.visibility = View.VISIBLE
                    }

                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        currentLocationFragmentBinding?.pbCurrentLocation?.visibility = View.GONE
                    }
                }
                is Resource.Loading -> {
                    currentLocationFragmentBinding?.pbCurrentLocation?.visibility = View.VISIBLE
                }
            }
        }
    }
}