package com.louis.gourmandism.profile

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

class ProfileViewModel(private val repository: Repository, private val userId: String) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _profile = MutableLiveData<User>()
    val profile: LiveData<User>
        get() = _profile

    private var _masterInfo = MutableLiveData<User>()
    val masterInfo: LiveData<User>
        get() = _masterInfo

    private var _comment = MutableLiveData<List<Comment>>()
    val comment: LiveData<List<Comment>>
        get() = _comment

    private var _shop = MutableLiveData<MutableList<Shop>>()
    val shop: LiveData<MutableList<Shop>>
        get() = _shop

    private var _myFavorite = MutableLiveData<MutableList<Favorite>>()
    val myFavorite: LiveData<MutableList<Favorite>>
        get() = _myFavorite

    private var _commentInfo = MutableLiveData<Comment>()
    val commentInfo: LiveData<Comment>
        get() = _commentInfo

    private var _shopInfo = MutableLiveData<Shop>()
    val shopInfo: LiveData<Shop>
        get() = _shopInfo

    private var _addFriendStatus = MutableLiveData<Boolean>()
    val addFriendStatus: LiveData<Boolean>
        get() = _addFriendStatus


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getProfile()
        getMasterInfo()
        getUserComment()
        getMyFavorite()
    }

    private fun getProfile() {
        coroutineScope.launch {
            userId.let {
                val result = repository.getUser(it)
                _profile.value = when (result) {
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
    private fun getMasterInfo() {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.getUser(it)
                _masterInfo.value = when (result) {
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

    private fun getUserComment() {
        coroutineScope.launch {
            userId.let {
                val result = repository.getComment(it, 1)
                _comment.value = when (result) {
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


    fun getShop(list: MutableList<BrowseRecently>) {
        coroutineScope.launch {
            val result = repository.getShop("", 0)
            _shop.value = when (result) {
                is Result.Success -> {
                    list.sortByDescending { it.time }
                    val shopList = mutableListOf<Shop>()
                    for ((id) in list) {
                        shopList.addAll(result.data.filter { it.id == id })
                    }
                    shopList
                }
                else -> {
                    null
                }
            }
        }
    }

    private fun getMyFavorite() {
        coroutineScope.launch {
            userId.let {
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

    fun setFriend(status: Boolean) {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.addFriend(it, userId, status)
                _addFriendStatus.value = when (result) {
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

    fun getLikeAmount(commentList: List<Comment>): Int{
        var likeAmount = 0
        for(comment in commentList){
            comment.like?.let {
                likeAmount += it.size
            }
        }
        return likeAmount
    }

    fun getForkAmount(favoriteList: List<Favorite>): Int{
        var forkAmount = 0
        for(favorite in favoriteList){
            favorite.attentionList?.let {
                forkAmount += it.size
            }
        }
        return forkAmount
    }


    fun navigateToComment(comment: Comment){
        _commentInfo.value = comment
    }

    fun navigateToShop(shop: Shop){
        _shopInfo.value = shop
    }

    fun onNavigated(){
        _commentInfo.value = null
        _shopInfo.value = null
    }


}