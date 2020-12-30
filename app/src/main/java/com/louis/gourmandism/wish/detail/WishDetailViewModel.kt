package com.louis.gourmandism.wish.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.*
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
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

    private val _listStatus = MutableLiveData<Int>()
    val listStatus: LiveData<Int>
        get() = _listStatus

    private val _setAttentionStatus = MutableLiveData<Boolean>()
    val setAttentionStatus: LiveData<Boolean>
        get() = _setAttentionStatus

    private val _removeListStatus = MutableLiveData<Boolean>()
    val removeListStatus: LiveData<Boolean>
        get() = _removeListStatus

    private var _addWishStatus = MutableLiveData<Boolean>()
    val addWishStatus: LiveData<Boolean>
        get() = _addWishStatus

    private var _setShareStatus = MutableLiveData<Boolean>()
    val setShareStatus: LiveData<Boolean>
        get() = _setShareStatus

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

    fun getNewShop(shopIdList: MutableList<String>): MutableList<Shop> {
        val favoriteShopList = mutableListOf<Shop>()
        _shop.value?.let {
            for (shopId in shopIdList) {
                if (it.any { shop -> shop.id == shopId }) {
                    favoriteShopList.add(it.filter { shop -> shop.id == shopId }[0])
                }
            }

        }
        return favoriteShopList
    }

    fun setAttention(status: Boolean) {

        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.setAttention(it, favorite.id, status)
                _setAttentionStatus.value = when(result){
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

    fun removeList(){
        coroutineScope.launch {
            val result = repository.removeWishList(favorite.id)
            _removeListStatus.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun setShare(status: Int){
        coroutineScope.launch {
            val result = repository.setShareStatus(favorite.id, status)
            _setShareStatus.value = when (result) {
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun checkListStatus(favorite: Favorite) {
        //1:自己的清單 2:已追隨清單 3:推薦清單
        _listStatus.value = if(favorite.userId == UserManager.userToken){
            1
        } else {
            if (favorite.type == 1 && favorite.attentionList!!.any { attention -> attention == UserManager.userToken }) {
                2
            } else if (favorite.type == 1 && !favorite.attentionList!!.any { attention -> attention == UserManager.userToken }) {
                3
            } else {
                4
            }
        }
    }

    fun setWish(shopId: String,status: Int) {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.setWish(favorite.id, shopId, status)
                _addWishStatus.value = when (result) {
                    is Result.Success -> {
                        updateList(shopId)
                        result.data
                    }
                    else -> {
                        null
                    }
                }
            }

        }
    }

    private fun updateList(shopId: String){
        val list = favoriteInfo.value?.copy()
        list?.shops?.remove(shopId)
        _favoriteInfo.value = list
    }

    fun navigateToDetail(shop: Shop) {
        _navigateInfo.value = shop
    }

    fun onNavigationDone() {
        _navigateInfo.value = null
    }

}