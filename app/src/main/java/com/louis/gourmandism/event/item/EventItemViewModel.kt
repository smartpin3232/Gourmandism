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

    init {
//        getEvent(status)
        getLiveEvent(status)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


//    private fun getEvent(status: Int){
//
//        coroutineScope.launch {
//
//            val result = repository.getEvent(status)
//            _eventList.value = when(result){
//                is Result.Success -> {
//                    when(status){
//                        0->result.data
//                        else->filter(result.data,"Louis")
//                    }
//                }
//                else -> {
//                    null
//                }
//            }
//
//
//        }
//    }

    private fun getLiveEvent(status: Int) {
        liveEventList = repository.getLiveEvents(status)
    }


    fun joinGame(eventId: String, userId: String) {
        coroutineScope.launch {

            val result = repository.joinGame(eventId, userId)
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


    private fun filter(list: List<Event>, userId: String): List<Event> {

        val lowerCaseQueryString = userId.toLowerCase(Locale.ROOT)
        val filteredList = mutableListOf<Event>()

        for (event in list) {

            event.member.let { member ->
                if (member != null && member.any { it.toLowerCase(Locale.ROOT) == lowerCaseQueryString }) {
                    filteredList.add(event)
                }
            }
        }
        return filteredList
    }

    fun toShop(item: Event) {
        _shopInfo.value = item.shop
    }


}