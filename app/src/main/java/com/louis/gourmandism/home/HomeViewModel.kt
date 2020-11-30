package com.louis.gourmandism.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.louis.gourmandism.data.Result

class HomeViewModel(private val repository: Repository) : ViewModel(){

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _navigationStatus =MutableLiveData<String>()
    val navigationStatus: LiveData<String>
        get() = _navigationStatus

    private var _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    init {
        getComment()
    }

    fun getComment(){
        coroutineScope.launch {
            val result = repository.getComment()
            _commentList.value = when(result){
                is Result.Success -> {
                    result.data
                }
                else -> {
                    Log.i("getComment","Error")
                    null
                }
            }
        }
    }

    fun navigationToDetail(item:String){
        _navigationStatus.value = item
    }

    fun onNavigated() {
        _navigationStatus.value = null
    }

}