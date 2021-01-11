package com.louis.gourmandism.add2comment

import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
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
        value = 0F
    }
    val star: LiveData<Float>
        get() = _star


    private var _profile = MutableLiveData<User>()
    val profile: LiveData<User>
        get() = _profile

    var imagesUri = MutableLiveData<MutableList<String>>()

    var localImages = MutableLiveData<MutableList<String>>()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getProfile()
    }

    private fun getProfile() {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.getUser(it)
                _profile.value = when (result) {
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

    fun setComment(comment: String, imageUri: MutableList<String>?){

        val imageArray = mutableListOf<String>()

        imageUri?.let {
            imageArray.addAll(it)
        }

        val commentInfo = Comment(
            host = profile.value,
            images = imageArray,
            shopId = shopInfo.value?.id!!,
            title = shopInfo.value?.name!!,
            content = comment,
            star = star.value!!,
            createdTime = System.currentTimeMillis()
        )

        coroutineScope.launch {
            val result = repository.newComment(commentInfo)
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

    fun setStar(list: MutableList<ImageView>,amount: Int){
        for (i in 0 until list.size){
            list[i].setBackgroundResource(R.drawable.tasty)
        }
        for (i in 0 until amount){
            list[i].setBackgroundResource(R.drawable.tasty_select)
        }
    }

    fun getStar(level: Int){
        _star.value = level.toFloat()
    }
}