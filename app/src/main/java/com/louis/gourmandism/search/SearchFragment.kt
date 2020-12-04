package com.louis.gourmandism.search

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.louis.gourmandism.R
import com.louis.gourmandism.bindImage
import com.louis.gourmandism.databinding.FragmentSearchBinding
import com.louis.gourmandism.extension.getVmFactory


class SearchFragment : Fragment(){

    private val viewModel by viewModels<SearchViewModel> { getVmFactory() }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermission = false
    var PERMISSION_ID = 8787
    private var myMap: GoogleMap? = null
    private var lastKnownLocation: Location? = null

    private val callback = OnMapReadyCallback { googleMap ->
        myMap = googleMap
        getLocationPermission()

        //TextView resolutionTextView = view.findViewById(R.id.video_preview_item_tv); // 找到整个大布局内的一个textView控件

        //將畫面移動到某個座標
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(23.0, 110.0)))
//        googleMap.setMinZoomPreference(5.0f)

        getDeviceLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        
        val adapter = SearchAdapter(viewModel)
        binding.recyclerViewTag.adapter = adapter

        val testTag = mutableListOf("炸物","火鍋","咖啡","黑暗料理","約會景點")

        binding.textTitle.setOnClickListener {
            Log.i("click","click")
            getDeviceLocation()
        }

        adapter.submitList(testTag)

        viewModel.shopList.observe(viewLifecycleOwner, Observer {


            it?.let {
                for(item in it){

                    val markerInflater: LayoutInflater? = layoutInflater
                    val view: View = markerInflater?.inflate(R.layout.item_map_marker, null) ?: View(null)

                    val location = LatLng(item.location!!.locationX, item.location!!.locationY)
                    val title = item.name
//                    val b =createCustomMarker("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRx47NcX3rUCKXCt4PAE1IPsusWNiOYDcsYQ&usqp=CAU")


                   val a = view.findViewById<View>(R.id.image_marker) as ImageView
                    bindImage(a, item.image?.get(0))

//                    Glide.with(requireContext())
//                        .load(item.image).fitCenter()
//                        .into(view.findViewById<View>(R.id.image_marker) as ImageView)


                    myMap?.let { map -> viewModel.setMapMarker(map, view, location, title,requireContext()) }

                }
                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

                mapFragment?.getMapAsync(callback)
            }
        })

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // 2. init fusedLocationProviderClient and set LocationServices object
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    // 1. check Permission and get user get permission
    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermission = false
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID)
        } else {
            locationPermission = true
        }
    }

    // 3. set map UI isMyLocationButton Enabled
    private fun updateLocationUI() {
        myMap?.apply {
            try {
                if (locationPermission) {
                    isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                } else {
                    isMyLocationEnabled = false
                    uiSettings.isMyLocationButtonEnabled = false
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    // 4. get permission and update LocationUI: set map UI isMyLocationButton Enabled
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_ID -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission = true
                    // set map UI isMyLocationButton Enabled
                    updateLocationUI()
                }
            }
        }
    }

    // 5. getDeviceLocation
    private fun getDeviceLocation() {
        try {
            if (locationPermission) {
                val locationRequest = fusedLocationProviderClient.lastLocation
                locationRequest.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {

                            myMap?.apply {
                                addMarker(MarkerOptions()
                                    .position(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude))
                                    .title("It's ME!!")
                                    .snippet("${lastKnownLocation!!.latitude}, ${lastKnownLocation!!.longitude}")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

                                moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude), 10f))
                            }
                        }
                    } else {
                        myMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }



}