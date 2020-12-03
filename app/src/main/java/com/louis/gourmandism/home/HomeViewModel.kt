package com.louis.gourmandism.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HomeViewModel(private val repository: Repository) : ViewModel(){

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigationStatus =MutableLiveData<String>()
    val navigationStatus: LiveData<String>
        get() = _navigationStatus

    var commentList = MutableLiveData<List<Comment>>()

    init {
        getLiveArticlesResult()
    }

    private fun getLiveArticlesResult() {
        commentList = repository.getLiveComments()
    }

    fun navigationToDetail(item:String){
        _navigationStatus.value = item
    }

    fun onNavigated() {
        _navigationStatus.value = null
    }

}