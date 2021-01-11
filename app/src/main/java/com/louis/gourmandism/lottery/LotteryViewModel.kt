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
import java.util.*

class LotteryViewModel(private val repository: Repository) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _allShop = MutableLiveData<List<Shop>>()
    val allShop: LiveData<List<Shop>>
        get() = _allShop

    private var _filterShopList = MutableLiveData<List<Shop>>()
    val filterShopList: LiveData<List<Shop>>
        get() = _filterShopList



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

    fun filterShop(query: String){
        if(query == "全部"){
            _filterShopList.value = _allShop.value
        } else {
            _allShop.value?.let {
                val shopList = mutableListOf<Shop>()
                for(shop in it){
                    shop.type?.let { type ->
                        if(type.any { value-> value == query }){
                            shopList.add(shop)
                        }
                    }
                }
                _filterShopList.value = shopList
            }
        }
    }


}