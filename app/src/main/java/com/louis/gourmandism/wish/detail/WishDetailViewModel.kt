package com.louis.gourmandism.wish.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Event
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WishDetailViewModel(private val repository: Repository, private val favorite: Favorite) : ViewModel() {

    val viewModelJob = Job()
    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _favoriteInfo = MutableLiveData<Favorite>().apply {
        value = favorite
    }

    val favoriteInfo: LiveData<Favorite>
        get() = _favoriteInfo

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user


    init {

    }

    fun getUser(userId: String){
        coroutineScope.launch {
            val result = repository.getUser(userId)
            _user.value = when(result){
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