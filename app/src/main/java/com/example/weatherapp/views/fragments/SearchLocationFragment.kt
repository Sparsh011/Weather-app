package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.SearchLocationFragmentBinding
import com.example.weatherapp.viewmodel.SearchLocationViewModel

class SearchLocationFragment : Fragment(R.layout.search_location_fragment) {
    private lateinit var searchLocationViewModel: SearchLocationViewModel
    private var mBinding : SearchLocationFragmentBinding? = null
    private val TAG = "SearchLocationFragment"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = SearchLocationFragmentBinding.inflate(inflater, container, false)
        return mBinding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchLocationViewModel = ViewModelProvider(this)[SearchLocationViewModel::class.java]

        context?.let {
            searchLocationViewModel.getCoordinates("London", it)
        }

    }
}