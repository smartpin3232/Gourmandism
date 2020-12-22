package com.louis.gourmandism.wish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WishViewModel(private val repository: Repository) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _myFavorite = MutableLiveData<MutableList<Favorite>>()

    val myFavorite: LiveData<MutableList<Favorite>>
        get() = _myFavorite

    private var _newFavorite = MutableLiveData<MutableList<Favorite>>()

    val newFavorite: LiveData<MutableList<Favorite>>
        get() = _newFavorite


    private var _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    private var _navigationData = MutableLiveData<Favorite>()

    val navigationData: LiveData<Favorite>
        get() = _navigationData

    private val _shop = MutableLiveData<List<Shop>>()
    val shop: LiveData<List<Shop>>
        get() = _shop

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getMyFavorite()
//        getShop()
    }

    private fun getMyFavorite() {
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

    fun getShop() {
        coroutineScope.launch {
            val result = repository.getShop("", 0)
            _shop.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun getNewFavorite() : List<Favorite>?{
        val favoriteShopList = myFavorite.value!!.filter { it.userId == UserManager.userToken }
            favoriteShopList?.let {

                for (favorite in it) {
                    favorite.shopsInfo = mutableListOf()
                    for (shopId in favorite.shops!!){
                        val shop = shop.value!!.filter { a-> a.id == shopId }[0].copy()
                        favorite.shopsInfo!!.add(shop)
                    }
                }
            }

        return favoriteShopList
    }

    fun getShareFavorite(status: Int) : List<Favorite>?{
        val favoriteShopList = when(status){
            0->{
                myFavorite.value?.filter {
                    it.type == 1 && it.attentionList!!.any {attention-> attention == UserManager.userToken }
                }
            }
            1->{
                myFavorite.value?.filter {
                    it.type == 1 && !it.attentionList!!.any {attention-> attention == UserManager.userToken }
                }
            }
            else -> {
                myFavorite.value
            }
        }

        favoriteShopList?.let {

            for (favorite in it) {
                favorite.shopsInfo = mutableListOf()
                for (shopId in favorite.shops!!){
                    val shop = shop.value!!.filter { a-> a.id == shopId }[0].copy()
                    favorite.shopsInfo!!.add(shop)
                }
            }
        }

        return favoriteShopList
    }

    fun onNavigationDone(){
        _navigationData.value = null
    }

    fun navigateToDetail(favorite: Favorite) {
        _navigationData.value = favorite
    }


}