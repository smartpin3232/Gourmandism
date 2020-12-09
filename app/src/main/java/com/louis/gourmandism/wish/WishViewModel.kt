package com.louis.gourmandism.wish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WishViewModel(private val repository: Repository) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _myFavorite = MutableLiveData<MutableList<Favorite>>()

    val myFavorite : LiveData<MutableList<Favorite>>
        get() = _myFavorite

    private var _user = MutableLiveData<User>()

    val user : LiveData<User>
        get() = _user

    private var _favoriteData = MutableLiveData<Favorite>()

    val favoriteData : LiveData<Favorite>
        get() = _favoriteData

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getMyFavorite("003")
    }

    private fun getMyFavorite(userId: String){
        coroutineScope.launch {
            val result = repository.getMyFavorite(userId)
            _myFavorite.value = when(result){
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun getProfile(id: String){
        coroutineScope.launch {
            val result = repository.getUser(id)
            _user.value = when(result){
                is Result.Success -> {
                    Log.i("UserViewModel",result.data.name)
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun navigateToDetail(favorite: Favorite){
        _favoriteData.value = favorite
    }



}