package com.example.weatherapp.views.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapp.R
import com.example.weatherapp.utils.Variables
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.util.*

class GetUserLocation : AppCompatActivity() {
    private var locationRequest: LocationRequest? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_user_location)

        locationRequest = LocationRequest.create();
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest!!.setInterval(5000);
        locationRequest!!.setFastestInterval(2000);

        getCurrentLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    getCurrentLocation();

                } else {

                    turnOnGPS();
                }
            }
        }

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this@GetUserLocation,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (isGPSEnabled()) {
                locationRequest?.let {
                    LocationServices.getFusedLocationProviderClient(this@GetUserLocation)
                        .requestLocationUpdates(it, object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                super.onLocationResult(locationResult)
                                LocationServices.getFusedLocationProviderClient(this@GetUserLocation)
                                    .removeLocationUpdates(this)
                                if (locationResult.locations.size > 0) {
                                    val index = locationResult.locations.size - 1
                                    val latitude = locationResult.locations[index].latitude
                                    val longitude = locationResult.locations[index].longitude

                                    Variables.LATITUDE = latitude
                                    Variables.LONGITUDE = longitude

                                    val geocoder = Geocoder(this@GetUserLocation, Locale.getDefault())
                                    val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

                                    val cityName: String = addresses!![0].locality
                                    val stateName : String = addresses[0].adminArea
                                    val countryName: String = addresses[0].countryName

                                    Variables.CITY_NAME = cityName
                                    Variables.STATE_NAME = stateName
                                    Variables.COUNTRY_NAME = countryName

                                    startActivity(Intent(this@GetUserLocation, MainActivity::class.java))
                                }
                            }
                        }, Looper.getMainLooper())
                }
            } else {
                turnOnGPS()
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun turnOnGPS() {
        val builder = locationRequest?.let {
            LocationSettingsRequest.Builder().addLocationRequest(it)
        }

        builder?.setAlwaysShow(true)

        val result: Task<LocationSettingsResponse> = builder?.let {
            LocationServices.getSettingsClient(applicationContext).checkLocationSettings(it.build())
        } as Task<LocationSettingsResponse>

        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                Toast.makeText(this@GetUserLocation, "GPS is already turned on", Toast.LENGTH_SHORT).show()
            }
            catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(this@GetUserLocation, 2)
                    } catch (ex: IntentSender.SendIntentException) {
                        ex.printStackTrace()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    private fun isGPSEnabled(): Boolean {
        var locationManager: LocationManager? = null
        var isEnabled = false
        if (locationManager == null) {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isEnabled

    }
}