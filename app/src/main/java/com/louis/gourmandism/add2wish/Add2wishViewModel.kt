package com.louis.gourmandism.add2wish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class Add2wishViewModel(private val repository: Repository, private val shopId: String) : ViewModel() {


    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _myFavorite = MutableLiveData<MutableList<Favorite>>()

    val myFavorite: LiveData<MutableList<Favorite>>
        get() = _myFavorite


    private var _addWishStatus = MutableLiveData<Boolean>()

    val addWishStatus: LiveData<Boolean>
        get() = _addWishStatus

    init {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
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

    fun setWish(folderId: String,status: Int) {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.setWish(folderId, shopId, status)
                _addWishStatus.value = when (result) {
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

    fun getMyFavorite(favoriteList: MutableList<Favorite>): List<Favorite> {
        var myList = listOf<Favorite>()
        UserManager.userToken?.let{
            myList = favoriteList.filter {favorite ->  favorite.userId == it }
        }
        return myList
    }



}