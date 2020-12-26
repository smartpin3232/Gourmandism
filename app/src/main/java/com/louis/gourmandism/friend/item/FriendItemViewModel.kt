package com.louis.gourmandism.friend.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class FriendItemViewModel(private val repository: Repository) : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _profile = MutableLiveData<User>()
    val profile: LiveData<User>
        get() = _profile

    private var _friendInfo = MutableLiveData<List<User>>()
    val friendInfo: LiveData<List<User>>
        get() = _friendInfo

    var navigateProfile = MutableLiveData<User>()

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

    fun getAllFriend(friendList: List<String>) {
        coroutineScope.launch {
            UserManager.userToken?.let {
                val result = repository.getAllFriend(friendList)
                _friendInfo.value = when (result) {
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

    fun navigateToProfile(user: User){
        navigateProfile.value = user
    }

    fun onNavigateDone(){
        navigateProfile.value = null
    }
}