package com.louis.gourmandism


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.User
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.login.UserManager

import com.louis.gourmandism.util.CurrentFragmentType
import com.louis.gourmandism.util.DrawerToggleType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel(){

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    val profile = MutableLiveData<User>()

    val currentDrawerToggleType: LiveData<DrawerToggleType> = Transformations.map(currentFragmentType) {
        when (it) {
            CurrentFragmentType.LOTTERY -> DrawerToggleType.BACK
            CurrentFragmentType.WISH_DETAIL -> DrawerToggleType.BACK
            CurrentFragmentType.COMMENT -> DrawerToggleType.BACK
            else -> DrawerToggleType.NORMAL
        }
    }


    init {
        getProfile()
    }

    fun getProfile(){
        coroutineScope.launch {
            UserManager.userToken?.let {

                val result = repository.getUser(it)
                UserManager.user.value = when (result) {
                    is Result.Success -> {
                        profile.value = result.data
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