package com.louis.gourmandism.event.create


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Event
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.Member

class NewEventViewModel(private val repository: Repository, private val shop: Shop) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _event = MutableLiveData<Boolean>()
    val event: LiveData<Boolean>
        get() = _event

    private var _profile = MutableLiveData<User>()
    val profile : LiveData<User>
        get() = _profile

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getProfile("001")
    }

    fun newEvent(context: String,memberLimit: String){
        val eventInfo = Event(
            host =_profile.value,
            content = context,
            shop = shop,
            status = 0,
            member= mutableListOf(_profile.value?.name!!),
            memberLimit = memberLimit.toInt()
        )
        coroutineScope.launch {
            val result = repository.newEvent(eventInfo)
            _event.value = when(result){
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
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
}