package com.louis.gourmandism.wish.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewWishListDialogViewModel(private val repository: Repository) : ViewModel() {

    val viewModelJob = Job()
    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _setWishStatus = MutableLiveData<Boolean>()
    val setWishStatus: LiveData<Boolean>
        get() = _setWishStatus


    fun createWishList(folderName: String){

        val favorite = Favorite(
            attentionList = mutableListOf(),
            folderName = folderName,
            shops = mutableListOf(),
            userId = UserManager.userToken.toString()
        )

        coroutineScope.launch {
            val result = repository.newWishList(favorite)
            _setWishStatus.value = when (result) {
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