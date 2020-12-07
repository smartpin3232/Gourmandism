package com.louis.gourmandism.extension

import androidx.fragment.app.Fragment
import com.louis.gourmandism.MyApplication
import com.louis.gourmandism.add2comment.Add2commentDialogViewModel
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.factory.*
import com.louis.gourmandism.wish.detail.WishDetailViewModel

fun Fragment.getVmFactory(): ViewModelFactory {

    val repository = (requireContext().applicationContext as MyApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(status: Int): EventItemViewModelFactory {

    val repository = (requireContext().applicationContext as MyApplication).repository
    return EventItemViewModelFactory(repository, status)
}

fun Fragment.getVmFactory(favorite: Favorite): WishDetailViewModelFactory {

    val repository = (requireContext().applicationContext as MyApplication).repository
    return WishDetailViewModelFactory(repository, favorite)
}

fun Fragment.getVmFactory(shop: Shop): Add2commentViewModelFactory {

    val repository = (requireContext().applicationContext as MyApplication).repository
    return Add2commentViewModelFactory(repository, shop)
}

fun Fragment.getEventVmFactory(shop: Shop): NewEventViewModelFactory {

    val repository = (requireContext().applicationContext as MyApplication).repository
    return NewEventViewModelFactory(repository, shop)
}