package com.louis.gourmandism.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(){

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigationStatus =MutableLiveData<Boolean>()
    val navigationStatus: LiveData<Boolean>
        get() = _navigationStatus

    init {

    }

    fun getComment(){
        coroutineScope.launch {

        }
    }

    fun navigationToDetail(){
        _navigationStatus.value = true
    }

    fun onNavigated() {
        _navigationStatus.value = null
    }

}