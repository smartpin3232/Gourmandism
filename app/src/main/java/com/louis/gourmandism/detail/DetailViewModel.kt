package com.louis.gourmandism.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.BrowseRecently
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel(){

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _shopInfo = MutableLiveData<Shop>()
    val shopInfo: LiveData<Shop>
        get() = _shopInfo

    private var _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    private val _leaveDetail = MutableLiveData<Boolean>()
    val leaveDetail: LiveData<Boolean>
        get() = _leaveDetail

    private val _browserStatus = MutableLiveData<Boolean>()
    val browserStatus: LiveData<Boolean>
        get() = _browserStatus

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {

    }

    fun getShop(id: String,mode: Int){
        coroutineScope.launch {
            val result = repository.getShop(id,mode)
            _shopInfo.value = when(result){
                    is Result.Success -> {
                        if(result.data.isNotEmpty())   {
                            result.data[0]
                        }else{
                            null
                        }
                    }
                    else -> {
                        Log.i("getComment","Error")
                        null
                    }
            }
            Log.i("DetailViewModel","${_shopInfo}")
        }
    }

    fun getComment(id: String){
        coroutineScope.launch {
            val result = repository.getComment(id,0)
            _commentList.value = when(result){
                is Result.Success -> {
                    if(result.data.isNotEmpty())   {
                        result.data.filter { it.shopId == id }
                    }else{
                        null
                    }
                }
                else -> {
                    Log.i("getComment","Error")
                    null
                }
            }
            Log.i("DetailViewModel","${_shopInfo}")
        }
    }

    fun setBrowserHistory(shopId: String){

        val browseRecently = BrowseRecently(
            shopId = shopId
        )
        coroutineScope.launch {
            val result = repository.setBrowserHistory(UserManager.userToken.toString(),browseRecently)
            _browserStatus.value = when(result){
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    fun leaveDetail(){
        _leaveDetail.value = true
    }

}