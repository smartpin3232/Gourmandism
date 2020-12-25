package com.louis.gourmandism.event.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

class EventItemViewModel(private val repository: Repository, private val status: Int) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var liveEventList = MutableLiveData<List<Event>>()

    private var _shopInfo = MutableLiveData<Shop>()

    val shopInfo: LiveData<Shop>
        get() = _shopInfo

    private var joinStatus = MutableLiveData<Boolean>()

    private var _navigateToNewCommentInfo = MutableLiveData<Shop>()

    val navigateToNewCommentInfo: LiveData<Shop>
        get() = _navigateToNewCommentInfo


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
                joinStatus.value = when (result) {
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

    fun filterList(eventList: List<Event>): List<Event>{
        return if (status == 0){
            eventList.filter { it.time > System.currentTimeMillis() }
        } else {
            eventList
        }
    }

    fun toast(){
        _toastStatus.value = true
    }

    fun toShop(item: Event) {
        _shopInfo.value = item.shop
    }

    fun navigateToNewComment(shop: Shop){
        _navigateToNewCommentInfo.value = shop
    }

    fun onNavigationDone() {
        _shopInfo.value = null
        _navigateToNewCommentInfo.value = null
    }

    fun setEventNotification(event: Event){
        _notificationInfo.value = event
    }

}