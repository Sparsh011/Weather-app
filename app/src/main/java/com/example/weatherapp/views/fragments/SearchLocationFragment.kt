package com.example.weatherapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.SearchLocationFragmentBinding
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.viewmodel.SearchLocationViewModel


class SearchLocationFragment : Fragment(R.layout.search_location_fragment) {
    private lateinit var searchLocationViewModel: SearchLocationViewModel
    private var mBinding : SearchLocationFragmentBinding? = null
    private val TAG = "SearchLocationFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = SearchLocationFragmentBinding.inflate(inflater, container, false)
        return mBinding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        searchLocationViewModel = ViewModelProvider(this)[SearchLocationViewModel::class.java]

        mBinding?.btnGetWeatherCityName?.setOnClickListener{
            context?.let {
                val query = mBinding?.etCityName?.text.toString()
                searchLocationViewModel.getCoordinates(query.lowercase(), it)
            }
        }

        observeLiveData()
    }

    private fun observeLiveData(){
        searchLocationViewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
            when(response){
                is Resource.Loading -> {
                    mBinding?.pbLoadCustomCityWeather?.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    mBinding?.tvCityWeather?.text = response.data?.main?.temp.toString()
                    mBinding?.pbLoadCustomCityWeather?.visibility = View.GONE
                    mBinding?.tvCityWeather?.visibility = View.VISIBLE

                    var temp = response.data?.main?.temp
                    temp = temp?.minus(273)
                    temp = temp?.toInt()?.toDouble()

                    mBinding?.tvCityWeather?.text = temp.toString() + "C"

                    mBinding?.llUnits?.visibility = View.VISIBLE

                    mBinding?.btnKelvin?.setOnClickListener{
                        val newTemp = temp?.plus(273.0)
                        mBinding?.tvCityWeather?.text = newTemp.toString() + "K"
                    }

                    mBinding?.btnFahrenheit?.setOnClickListener{
                        temp?.let {
                            val newTemp = temp * 9 / 5 + 32
                            mBinding?.tvCityWeather?.text = newTemp.toString() + "F"
                        }
                    }

                    mBinding?.btnCelsius?.setOnClickListener{
//                        val newTemp = temp?.plus(273.0)
                        mBinding?.tvCityWeather?.text = temp.toString() + "C"
                    }


                    if (temp != null) {
                        if (temp > 30){
                            mBinding?.tvEmoji?.text = "\uD83E\uDD75"
                        }
                        else if (temp > 10 && temp < 30){
                            mBinding?.tvEmoji?.text = "\uD83D\uDE04"
                        }
                        else{
                            mBinding?.tvEmoji?.text = "\uD83E\uDD76\n"
                        }

                        mBinding?.tvEmoji?.visibility = View.VISIBLE
                    }
                }

                is Resource.Error -> {
                    mBinding?.pbLoadCustomCityWeather?.visibility = View.GONE
                    Toast.makeText(context, "Error Getting Temperature...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}