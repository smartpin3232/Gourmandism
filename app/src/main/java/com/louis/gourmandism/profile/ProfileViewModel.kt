package com.louis.gourmandism.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.*
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.data.source.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository : Repository) : ViewModel(){

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _profile = MutableLiveData<User>()
    val profile : LiveData<User>
        get() = _profile

    private var _comment = MutableLiveData<List<Comment>>()
    val comment : LiveData<List<Comment>>
        get() = _comment



    init {
        getProfile("002")
        getUserComment("Emil")
    }

    private fun getProfile(id: String){
        coroutineScope.launch {
            val result = repository.getUser(id)
            _profile.value = when(result){
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    private fun getUserComment(id: String){
        coroutineScope.launch {
            val result = repository.getComment(id,1)
            _comment.value = when(result){
                is Result.Success -> {
                    Log.i("getUserComment","${result.data}")
                    result.data
                }
                else -> {
                    Log.i("getUserComment","Error")
                    null
                }
            }
        }
    }

    private var _shop = MutableLiveData<MutableList<Shop>>()
    val shop : LiveData<MutableList<Shop>>
        get() = _shop


     fun getShop(list: MutableList<BrowseRecently>){
        coroutineScope.launch {
            val result = repository.getShop("",0)
            _shop.value = when(result){
                is Result.Success -> {
                    Log.i("getUserComment","${result.data}")
                    val shopList = mutableListOf<Shop>()
                    for ((id) in list){
                        shopList.addAll(result.data.filter { it.id == id})
                    }
                    shopList
                }
                else -> {
                    Log.i("getUserComment","Error")
                    null
                }
            }
        }
    }

}