package com.louis.gourmandism.wish.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.*
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WishDetailViewModel(private val repository: Repository, private val favorite: Favorite) :
    ViewModel() {

    val viewModelJob = Job()
    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _favoriteInfo = MutableLiveData<Favorite>().apply {
        value = favorite
    }

    val favoriteInfo: LiveData<Favorite>
        get() = _favoriteInfo

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _shop = MutableLiveData<List<Shop>>()
    val shop: LiveData<List<Shop>>
        get() = _shop

    private val _navigateInfo = MutableLiveData<Shop>()
    val navigateInfo: LiveData<Shop>
        get() = _navigateInfo

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getShop()
    }

    fun getUser(userId: String) {
        coroutineScope.launch {
            val result = repository.getUser(userId)
            _user.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
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

    fun getNewShop(shopIdList: MutableList<String>) : MutableList<Shop>{
        val favoriteShopList = mutableListOf<Shop>()
        _shop.value?.let {
            for (shopId in shopIdList) {
                if(it.any{shop ->  shop.id == shopId}){
                    favoriteShopList.add(it.filter { shop ->  shop.id == shopId }[0])
                }
            }

        }
        return favoriteShopList
    }


    fun navigateToDetail(shop: Shop) {
        _navigateInfo.value = shop
    }
}