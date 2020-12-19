package com.louis.gourmandism.event.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Event
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class EventItemViewModel(private val repository: Repository, status: Int) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _eventList = MutableLiveData<List<Event>>()

    val eventList: LiveData<List<Event>>
        get() = _eventList

    var liveEventList = MutableLiveData<List<Event>>()

    private var _shopInfo = MutableLiveData<Shop>()

    val shopInfo: LiveData<Shop>
        get() = _shopInfo

    private var _joinStatus = MutableLiveData<Boolean>()

    val joinStatus: LiveData<Boolean>
        get() = _joinStatus

    private var _toastStatus = MutableLiveData<Boolean>()

    val toastStatus: LiveData<Boolean>
        get() = _toastStatus

    private var _notificationInfo = MutableLiveData<Event>()

    val notificationInfo: LiveData<Event>
        get() = _notificationInfo

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getLiveEvent(status)
    }


    private fun getLiveEvent(status: Int) {
        liveEventList = repository.getLiveEvents(status)
    }


    fun joinGame(eventId: String, status: Int) {
        coroutineScope.launch {

            UserManager.userToken?.let {
                val result = repository.joinGame(eventId, it, status)
                _joinStatus.value = when (result) {
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

    fun toast(){
        _toastStatus.value = true
    }

    fun toShop(item: Event) {
        _shopInfo.value = item.shop
    }

    fun setEventNotification(event: Event){
        _notificationInfo.value = event
    }


}