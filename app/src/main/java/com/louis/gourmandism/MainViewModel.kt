package com.louis.gourmandism

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.data.source.Repository
import com.louis.gourmandism.util.CurrentFragmentType

class MainViewModel(private val repository: Repository) : ViewModel(){

    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

}