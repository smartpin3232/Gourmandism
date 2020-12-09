package com.louis.gourmandism.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
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

    fun leaveDetail(){
        _leaveDetail.value = true
    }

}