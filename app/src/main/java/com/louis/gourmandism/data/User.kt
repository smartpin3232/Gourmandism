package com.louis.gourmandism.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var location: String = "",
    var image: String = "",
    var currentPosition: String = "",
    var browseRecently: MutableList<BrowseRecently>? = null,
    var friendList: MutableList<String>? = null,
    var selectTags: MutableList<String>? = null
): Parcelable
