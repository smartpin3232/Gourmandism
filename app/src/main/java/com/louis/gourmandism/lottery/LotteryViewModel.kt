package com.louis.gourmandism.lottery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LotteryViewModel(private val repository: Repository) : ViewModel() {


    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _allShop = MutableLiveData<List<Shop>>()
    val allShop: LiveData<List<Shop>>
        get() = _allShop

    var lotteryShop = MutableLiveData<Shop>()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getShop()
    }

    private fun getShop(){
        coroutineScope.launch {
            val result = repository.getShop("",0)
            _allShop.value = when(result){
                is Result.Success->{
                    result.data
                }
                else -> null
            }
        }
    }
}