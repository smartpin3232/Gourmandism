package com.louis.gourmandism.login

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel(){

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _createStatus = MutableLiveData<Boolean>()

    val createStatus : LiveData<Boolean>
        get() = _createStatus

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {

    }

    fun createUser(googleUid: String, name: String, photo: Uri?){
        coroutineScope.launch {
            val userInfo = User(
                id = googleUid,
                name = name,
                image = photo.toString()
            )
            val result = repository.createUser(userInfo)
            _createStatus.value = when(result){
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