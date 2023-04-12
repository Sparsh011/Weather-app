package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CurrentLocationFragmentBinding
import com.example.weatherapp.viewmodel.CurrentLocationViewModel

class CurrentLocationFragment : Fragment(R.layout.current_location_fragment) {
    private var currentLocationFragmentBinding : CurrentLocationFragmentBinding? = null
    private var currentLocationViewModel: CurrentLocationViewModel? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentLocationFragmentBinding = CurrentLocationFragmentBinding.inflate(inflater, container, false)
        return currentLocationFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentLocationViewModel = ViewModelProvider(this)[CurrentLocationViewModel::class.java]

//        Use DataClass.Main class to get temp
//        currentLocationViewModel?.currentWeatherResponse.observe()
//        currentLocationViewModel.getCurrentWeather(context?)

        context?.let {
            currentLocationViewModel?.getCurrentWeather(it)
        }
    }
}