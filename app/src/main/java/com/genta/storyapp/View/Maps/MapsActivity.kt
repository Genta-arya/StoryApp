package com.genta.storyapp.View.Maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.genta.storyapp.Model.UserPreference
import com.genta.storyapp.R
import com.genta.storyapp.ViewModelFactory
import com.genta.storyapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val Context.dataStores: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var map:GoogleMap
    private lateinit var binding : ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapViewModel: MapViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupView()
        setupVm()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        myLastLocation()
    }

    private fun setupVm(){
        mapViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStores))
        )[MapViewModel::class.java]
        mapViewModel.APImessage.observe(this,{mess->
            Toast.makeText(this,mess, Toast.LENGTH_SHORT).show()
        })

        mapViewModel.getUser().observe(this,{user->
            Log.d("ActivityMain","isLogin: ${user.isLogin}")
            if(user.isLogin){
                user.token.let { mapViewModel.getAllDataLocation(user.token) }
            }
        })
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setAdapter()
    }

    private fun setAdapter(){
        mapViewModel.listStory.observe(this,{
            for (item in 0..it.size-1){
                val listStory = it[item]
                Log.d("Data Map: ",listStory.lat.toString() +" lon ="+ listStory.lon.toString())
                var location = LatLng(listStory.lat!!,listStory.lon!!)
                var geofenceRadius = 400.0
                map.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(listStory.name)
                        .snippet("${listStory.lat}, ${listStory.lon}")
                )
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                map.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(geofenceRadius)
                        .fillColor(0x22FF0000)
                        .strokeColor(Color.RED)
                        .strokeWidth(3f)
                )

            }
        })
    }
    private fun showMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        map.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title(getString(R.string.location))
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
    }
    private fun myLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showMarker(location)
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "Lokasi tidak di temukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    myLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    myLastLocation()
                }
                else -> {
                    ""
                }
            }
        }

    private fun setupView() {
        supportActionBar?.hide()
    }
}