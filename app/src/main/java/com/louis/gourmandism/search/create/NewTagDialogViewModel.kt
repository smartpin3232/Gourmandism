package com.louis.gourmandism.search.create


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewTagDialogViewModel(private val repository: Repository) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _tagStatus = MutableLiveData<Boolean>()
    val tagStatus: LiveData<Boolean>
        get() = _tagStatus

    init {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun setUserTag(tag: String) {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.setSelectTag(it, tag, true)
                _tagStatus.value = when (result) {
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

    fun getProfile(){
        coroutineScope.launch {
            UserManager.userToken?.let {

                val result = repository.getUser(it)
                UserManager.user.value = when (result) {
                    is Result.Success -> {
                        Log.i("UserViewModel", result.data.name)
                        result.data
                    }
                    else -> {
                        null
                    }
                }
            }

        }
    }

}