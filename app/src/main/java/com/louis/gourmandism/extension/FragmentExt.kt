package com.louis.gourmandism.extension

import androidx.fragment.app.Fragment
import com.louis.gourmandism.MyApplication
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.data.Location
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.factory.*


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

fun Fragment.getCommentVmFactory(comment: Comment): CommentViewModelFactory {

    val repository = (requireContext().applicationContext as MyApplication).repository
    return CommentViewModelFactory(repository, comment)
}

fun Fragment.newShopVmFactory(location: Location): NewShopViewModelFactory {

    val repository = (requireContext().applicationContext as MyApplication).repository
    return NewShopViewModelFactory(repository, location)
}