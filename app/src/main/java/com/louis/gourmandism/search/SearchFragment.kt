package com.louis.gourmandism.search

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController


import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Shop

import com.louis.gourmandism.databinding.FragmentSearchBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.login.UserManager
import com.louis.gourmandism.search.create.NewTagDialog


class SearchFragment : Fragment(){

    private val viewModel by viewModels<SearchViewModel> { getVmFactory() }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermission = false
    var PERMISSION_ID = 8787
    private var myMap: GoogleMap? = null
    private var lastKnownLocation: Location? = null

    var clickMarker : Marker? = null

    override fun onPause() {
        super.onPause()
        Log.i("SearchFragment","onPause")
    }

    override fun onStart() {
        super.onStart()
        Log.i("SearchFragment","onStart")
    }

    private val callback = OnMapReadyCallback { googleMap ->
        myMap = googleMap
        getLocationPermission()

        updateLocationUI()

        //將畫面移動到某個座標
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(23.0, 110.0)))

        //畫面遠近 1~20F
//        googleMap.setMinZoomPreference(5.0f)

        getDeviceLocation()

        //Marker點擊事件
        googleMap.setOnMarkerClickListener {

            if (it != clickMarker){
                viewModel.getShop(it.tag.toString(),1)
                binding.cardShopInfo.visibility = View.VISIBLE
            }else{
                binding.cardShopInfo.visibility = View.GONE
            }
            false
        }


        //地圖點擊事件
        googleMap.setOnMapClickListener {
            binding.cardShopInfo.visibility = View.GONE

            if(clickMarker?.title != "新增餐廳"){
                clickMarker = googleMap.addMarker(MarkerOptions().position(it).title("新增餐廳"))
            }else{
                clickMarker?.position = it
                Log.i("position",it.toString())
            }
        }

        googleMap.setOnInfoWindowClickListener {
            Log.i("InfoWindow",it.title)
            if(it.title == "新增餐廳"){
                val location = com.louis.gourmandism.data.Location(it.position.latitude,it.position.longitude)
                findNavController().navigate(SearchFragmentDirections.actionGlobalNewShopFragment(location))
            }
        }

    }

    private var mapFragment: SupportMapFragment? = null
    lateinit var binding : FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel= viewModel

        binding.cardShopInfo.bringToFront()
        val adapter = SearchAdapter(viewModel)
        binding.recyclerViewTag.adapter = adapter

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        viewModel.selectTagList.observe(viewLifecycleOwner, Observer {
            val list = mutableListOf<String>()
            list.addAll(it)
            list.add("新增")
            adapter.submitList(list)
            adapter.notifyDataSetChanged()
        })

        //點擊取得當前位置
        binding.textTitle.setOnClickListener {
            getDeviceLocation()
            viewModel.getShopList("",0)
        }

        viewModel.shopList.observe(viewLifecycleOwner, Observer {

            it?.let {

                for(item in it){
                    myMap?.let { map -> viewModel.setMapMarker(map,item,requireActivity()) }
                }
            }
        })

        UserManager.user.observe(viewLifecycleOwner, Observer {
            viewModel.getUserSelectTag()
        })

        viewModel.navigateToNewTag.observe(viewLifecycleOwner, Observer {

           findNavController().navigate(SearchFragmentDirections.actionGlobalNewTagDialog())
        })

        viewModel.filterShopList.observe(viewLifecycleOwner, Observer {
            resetMarker(it)
        })

        binding.cardShopInfo.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionGlobalDetailFragment(viewModel.shop.value?.id))
            Log.i("cardView","click")
        }

        binding.editSearch.addTextChangedListener {

            viewModel.shopList.value?.let {shopList->
                val filterList = viewModel.filter(shopList ,it.toString())
                resetMarker(filterList)
            }
        }

        return binding.root
    }

    private fun resetMarker(filterList: List<Shop>){
        myMap?.clear()

        for (item in filterList) {

            myMap?.let { map -> viewModel.setMapMarker(map,item,requireActivity()) }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                                        LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude), 15f))
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