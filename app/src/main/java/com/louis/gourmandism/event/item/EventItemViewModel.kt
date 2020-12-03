package com.louis.gourmandism.event.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Event
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EventItemViewModel(private val repository: Repository) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _eventList = MutableLiveData<List<Event>>()

    val eventList : LiveData<List<Event>>
        get() = _eventList

    private var _shopInfo = MutableLiveData<Shop>()

    val shopInfo : LiveData<Shop>
        get() = _shopInfo

    init {
        getEvent()
    }

    fun getEvent(){
        coroutineScope.launch {

            val result = repository.getEvent()
            _eventList.value = when(result){
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }


        }
    }

    fun getShop(id: String){
        coroutineScope.launch {
            val result = repository.getShop(id,1)
            _shopInfo.value = when(result){
                is Result.Success -> {
                    result.data[0]
                }
                else -> {
                    null
                }
            }
        }
    }

}