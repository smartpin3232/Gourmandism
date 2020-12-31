package com.louis.gourmandism.search

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.icu.math.BigDecimal
import android.location.Location
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //All shop list
    private var _shopList = MutableLiveData<List<Shop>>()
    val shopList: LiveData<List<Shop>>
        get() = _shopList

    //使用marker搜尋的list
    private var _markerFilterShopList = MutableLiveData<List<Shop>>()
    val markerFilterShopList: LiveData<List<Shop>>
        get() = _markerFilterShopList

    //show shop information when click marker
    private var _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop>
        get() = _shop

    private var _navigateToNewTag = MutableLiveData<Boolean>()
    val navigateToNewTag: LiveData<Boolean>
        get() = _navigateToNewTag

    //Get my favorite list
    private var _myFavorite = MutableLiveData<MutableList<Favorite>>()
    val myFavorite: LiveData<MutableList<Favorite>>
        get() = _myFavorite

    //Get my favorite shopId
    private var _myFavoriteShop = MutableLiveData<MutableList<String>>()
    val myFavoriteShop: LiveData<MutableList<String>>
        get() = _myFavoriteShop

    private var _tagResult = MutableLiveData<Boolean>()
    val tagResult: LiveData<Boolean>
        get() = _tagResult

    private var _navigateDetailInfo = MutableLiveData<Shop>()
    val navigateDetailInfo: LiveData<Shop>
        get() = _navigateDetailInfo

    private var _navigateAddWishInfo = MutableLiveData<Shop>()
    val navigateAddWishInfo: LiveData<Shop>
        get() = _navigateAddWishInfo

    private var tagStatus: String = ""

    var selectShopList = MutableLiveData<List<Shop>>()

    var tagPosition = MutableLiveData<Int>()

    var selectTagList = MutableLiveData<MutableList<String>>()

    var myLocation = MutableLiveData<LatLng>()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getUserSelectTag()
        getFavorite()
    }

    private fun getFavorite() {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.getMyFavorite(it)
                _myFavorite.value = when (result) {
                    is Result.Success -> {
                        result.data
                    }
                    else -> {
                        null
                    }
                }
            }

        }
    }

    fun getMyFavoriteShop(favoriteList: MutableList<Favorite>) {
        UserManager.userToken?.let {
            val newShopList = mutableListOf<String>()
            val shopList = favoriteList.filter { it.userId == UserManager.userToken }
            shopList.forEach {favorite->
                favorite.shops?.let { shop->
                    for(item in shop)
                        newShopList.add(item)
                }
            }
            _myFavoriteShop.value = newShopList
        }
    }

    fun getShopList(id: String, mode: Int) {
        coroutineScope.launch {
            val result = repository.getShop(id, 0)
            _shopList.value = when (result) {
                is Result.Success -> {
                    if (result.data.isNotEmpty()) {
                        result.data
                    } else {
                        null
                    }
                }
                else -> {
                    null
                }
            }

        }
    }

    fun getShop(id: String, mode: Int) {
        coroutineScope.launch {
            val result = repository.getShop(id, mode)
            _shop.value = when (result) {
                is Result.Success -> {
                    if (result.data.isNotEmpty()) {
                        result.data[0]
                    } else {
                        null
                    }
                }
                else -> {
                    null
                }
            }

        }
    }

    fun setUserTag(tag: String) {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.setSelectTag(it, tag, false)
                _tagResult.value = when (result) {
                    is Result.Success -> {
                        removeUserTag(tag)
                        result.data
                    }
                    else -> {
                        null
                    }
                }
            }
        }
    }

    fun getUserSelectTag() {
        val newTag = mutableListOf<String>()
        UserManager.user.value?.selectTags?.let { newTag.addAll(it) }
        selectTagList.value = newTag
    }

    private fun removeUserTag(tag: String){
        val newTags = mutableListOf<String>()
        UserManager.user.value?.selectTags?.let { tags->
            tags.remove(tag)
            newTags.addAll(tags)
        }
        selectTagList.value = newTags
    }

    fun addSelectTag() {
        _navigateToNewTag.value = true
    }

    fun setMapMarker(
        googleMap: GoogleMap,
        item: Shop,
        activity: Activity
    ) {
        coroutineScope.launch {

            val location = LatLng(item.location!!.locationX, item.location!!.locationY)
            val title = item.name

            Glide.with(activity)
                .asBitmap()
                .load(
                    when (item.image.isNullOrEmpty()) {
                        true -> R.drawable.camera
                        else -> item.image?.get(0)
                    }
                )
                .apply(
                    RequestOptions().transform(CenterCrop(), RoundedCorners(10))
                )
                .into(object : SimpleTarget<Bitmap>(100, 100) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        googleMap.addMarker(
                            MarkerOptions()
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        resource
                                    )
                                )
                                .position(location).title(title)
                        ).tag = item.id
                    }
                })
        }
    }

    fun filter(list: List<Shop>, query: String): List<Shop> {

        val lowerCaseQueryString = query.toLowerCase(Locale.ROOT)
        val filteredList = mutableListOf<Shop>()
        for (shop in list) {

            val name = shop.name.toLowerCase(Locale.ROOT) ?: ""
            val tag = shop.type

            if (tag != null) {
                if (name.contains(lowerCaseQueryString) || tag.any {
                        it.toLowerCase(Locale.ROOT).contains(lowerCaseQueryString)
                    }) {
                    filteredList.add(shop)
                }
            } else {
                if (name.contains(lowerCaseQueryString)) {
                    filteredList.add(shop)
                }
            }
        }
        return filteredList
    }

    fun markerSet(tag: String, position: Int) {
        if (tagStatus == tag) {
            _markerFilterShopList.value = selectShopList.value
            tagPosition.value = null
            tagStatus = ""
        } else {
            _markerFilterShopList.value = filter(selectShopList.value!!, tag)
            tagPosition.value = position
            tagStatus = tag
        }
    }

    fun getDistance(start: LatLng, end: LatLng): Double {

        val lat1 = Math.PI / 180 * start.latitude
        val lat2 = Math.PI / 180 * end.latitude
        val lon1 = Math.PI / 180 * start.longitude
        val lon2 = Math.PI / 180 * end.longitude

        //地球半徑
        val earthRadius = 6371.0

        //兩點間距離 km，如果想要米的話，結果*1000就可以了
        val distance = acos(
            sin(lat1) * sin(lat2) + cos(lat1) * cos(
                lat2
            ) * cos(lon2 - lon1)
        ) * earthRadius

        return distance
    }

    fun navigateToDetail(shopInfo: Shop){
        _navigateDetailInfo.value = shopInfo
    }

    fun navigateToAddWish(shop: Shop){
        _navigateAddWishInfo.value = shop
    }

    fun onNavigateDone(){
        _navigateDetailInfo.value = null
        _navigateAddWishInfo.value = null
    }

}