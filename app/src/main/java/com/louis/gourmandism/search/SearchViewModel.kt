package com.louis.gourmandism.search

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.louis.gourmandism.R
import com.louis.gourmandism.bindImage
import com.louis.gourmandism.data.Location
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.*
import java.util.*

class SearchViewModel(private val repository: Repository) :ViewModel(){


    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getShopList("",0)
    }


    private var _shopList = MutableLiveData<List<Shop>>()
    val shopList: LiveData<List<Shop>>
        get() = _shopList

    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() = _shop

    fun getShopList(id: String,mode: Int){
        coroutineScope.launch {
            val result = repository.getShop(id,0)
            _shopList.value = when(result){
                is Result.Success -> {
                    if(result.data.isNotEmpty())   {
                        result.data
                    }else{
                        null
                    }
                }
                else -> {
                    Log.i("getShop","Error")
                    null
                }
            }

        }
    }

    fun getShop(id: String,mode: Int){
        coroutineScope.launch {
            val result = repository.getShop(id,mode)
            _shop.value = when(result){
                is Result.Success -> {
                    if(result.data.isNotEmpty())   {
                        result.data[0]
                    }else{
                        null
                    }
                }
                else -> {
                    Log.i("getShop","Error")
                    null
                }
            }

        }
    }


    fun getShopList(): MutableList<Location> {

        val shopList = mutableListOf<Location>(
            Location(23.0, 110.0),
            Location(24.0, 120.0),
            Location(25.0, 130.0),
            Location(26.0, 100.0),
            Location(27.0, 140.0)
        )
        return shopList
    }

    fun setMapMarker(
        googleMap: GoogleMap,
        context:Context,
        layoutInflater:LayoutInflater,
        item: Shop
    ) {
        coroutineScope.launch {
            val markerInflater: LayoutInflater? = layoutInflater
            val view: View = markerInflater?.inflate(R.layout.item_map_marker, null) ?: View(null)

            val location = LatLng(item.location!!.locationX, item.location!!.locationY)
            val title = item.name

            val markerView = view.findViewById<View>(R.id.image_marker) as ImageView
            bindImage(markerView, item.image?.get(0))
            googleMap.addMarker(
                MarkerOptions()
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            createDrawableFromView(
                                context,
                                view
                            )
                        )
                    )
                    .position(location).title(title)
            ).tag = item.id

            delay(1000)
        }
    }

    private fun createDrawableFromView(context: Context, view: View): Bitmap? {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay
            .getMetrics(displayMetrics)


        view.layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)

        view.layout(
            0, 0, displayMetrics.widthPixels,
            displayMetrics.heightPixels
        )
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun filter(list: List<Shop>, query: String): List<Shop> {

        val lowerCaseQueryString = query.toLowerCase(Locale.ROOT)
        val filteredList = mutableListOf<Shop>()
        for (shop in list) {

            val name = shop.name ?: ""
            val tag = shop.type

            if (tag != null) {
                if (name.contains(lowerCaseQueryString) || tag.any { it.contains(lowerCaseQueryString) }) {
                    filteredList.add(shop)
                }
            }else{
                if (name.contains(lowerCaseQueryString)) {
                    filteredList.add(shop)
                }
            }
        }
        return filteredList
    }
}