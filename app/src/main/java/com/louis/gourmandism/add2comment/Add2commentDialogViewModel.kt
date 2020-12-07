package com.louis.gourmandism.add2comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class Add2commentDialogViewModel(private val repository: Repository, private val shop: Shop) : ViewModel() {


    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _shopInfo = MutableLiveData<Shop>().apply {
        value = shop
    }

    val shopInfo: LiveData<Shop>
        get() = _shopInfo

    private var _setStatus = MutableLiveData<Boolean>()

    val setStatus: LiveData<Boolean>
        get() = _setStatus

    private var _star = MutableLiveData<Float>().apply {
        value = 0.toFloat()
    }

    val star: LiveData<Float>
        get() = _star

    init {

    }


    fun setComment(comment: String){

        val commentInfo = Comment(
            hostId = "002",
            shopId = _shopInfo.value?.id!!,
            title = _shopInfo.value?.name!!,
            content = comment,
            star = _star.value!!
        )

        coroutineScope.launch {
            val result = repository.setComment(commentInfo)
            _setStatus.value = when(result){
                is Result.Success -> {
                    result.data
                }
                else -> {
                    null
                }
            }
        }
    }


    fun getStar(level: Int){
        _star.value = level.toFloat()
    }
}