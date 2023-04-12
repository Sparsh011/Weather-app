package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CurrentLocationFragmentBinding

class CurrentLocationFragment : Fragment(R.layout.current_location_fragment) {
    private var currentLocationFragmentBinding : CurrentLocationFragmentBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentLocationFragmentBinding = CurrentLocationFragmentBinding.inflate(inflater, container, false)
        return currentLocationFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}