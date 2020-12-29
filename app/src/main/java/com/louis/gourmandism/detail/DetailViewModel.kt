package com.louis.gourmandism.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.*
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.ExperimentalTime

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

    private var _newEventStatus = MutableLiveData<Boolean>()
    val newEventStatus: LiveData<Boolean>
        get() = _newEventStatus

    val date = MutableLiveData<Long>()

    var toProfileStatus = MutableLiveData<String>()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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
                        null
                    }
            }
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
                    null
                }
            }
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

    fun newEvent(content: String, memberLimit: String, createTime: Long){
        val eventInfo = Event(
            createTime = createTime,
            host = UserManager.user.value,
            content = content,
            shop = shopInfo.value,
            status = 0,
            time = date.value!!,
            member= mutableListOf(UserManager.user.value!!.id),
            memberLimit = memberLimit.toInt()
        )
        coroutineScope.launch {
            val result = repository.newEvent(eventInfo)
            _newEventStatus.value = when(result){
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }

    @ExperimentalTime
    fun getTodayBusinessTime(time: MutableList<OpenTime>, today: String) : OpenTime{
        return time.filter { it.day == today }[0]
    }

    fun checkBusinessStatus(businessTime: OpenTime): Boolean {
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm")
        val hours = cal.get(Calendar.HOUR_OF_DAY)
        val minutes= cal.get(Calendar.MINUTE)
        val nowTimeFormat = dateFormat.parse("$hours:$minutes")
        val startTime = dateFormat.parse(businessTime.startTime)
        val endTime = dateFormat.parse(businessTime.endTime)
        return nowTimeFormat in startTime..endTime
    }

    fun navigateToProfile(userId: String) {
        toProfileStatus.value = userId
    }

    fun leaveDetail(){
        _leaveDetail.value = true
    }

}