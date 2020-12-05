package com.louis.gourmandism.extension

import androidx.fragment.app.Fragment
import com.louis.gourmandism.MyApplication
import com.louis.gourmandism.factory.EventItemViewModelFactory
import com.louis.gourmandism.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {

    val repository = (requireContext().applicationContext as MyApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(status: Int): EventItemViewModelFactory {

    val repository = (requireContext().applicationContext as MyApplication).repository
    return EventItemViewModelFactory(repository, status)

}