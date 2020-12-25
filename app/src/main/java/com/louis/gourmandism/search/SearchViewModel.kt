package com.louis.gourmandism.search

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
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

    private var tagStatus: String = ""

    var selectTagList = MutableLiveData<MutableList<String>>()

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

    var selectShopList = MutableLiveData<List<Shop>>()

    var tagPosition = MutableLiveData<Int>()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getShopList("", 0)
        getUserSelectTag()
        getFavorite()
    }

    private fun getFavorite() {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.getMyFavorite(it)
                _myFavorite.value = when (result) {
                    is Result.Success -> {
                        Log.i("favorite",result.data.toString())
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
                    Log.i("getShop", "Error")
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
                    Log.i("getShop", "Error")
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
        UserManager.user.value?.selectTags?.remove(tag)
        val newTag = mutableListOf<String>()
        UserManager.user.value?.selectTags?.let { newTag.addAll(it) }
        selectTagList.value = newTag
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

    fun navigateToDetail(shopInfo: Shop){
        _navigateDetailInfo.value = shopInfo
    }

    fun onNavigateDone(){
        _navigateDetailInfo.value = null
    }

}