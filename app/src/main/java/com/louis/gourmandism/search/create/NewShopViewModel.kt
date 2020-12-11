package com.louis.gourmandism.search.create


import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Location
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewShopViewModel(private val repository: Repository, private val location: Location) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _newStatus = MutableLiveData<Boolean>()

    val newStatus: LiveData<Boolean>
        get() = _newStatus

    init {
    }

    fun newShop(name: String, address: String, phone: String){
        val shop= Shop(
            name = name,
            address = address,
            phone = phone,
            location = Location(location.locationX,location.locationY),
            image = mutableListOf()
        )
        coroutineScope.launch {
            val result = repository.newShop(shop)
            _newStatus.value = when(result){
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