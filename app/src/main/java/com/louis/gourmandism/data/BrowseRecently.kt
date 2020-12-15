package com.louis.gourmandism.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BrowseRecently(
    var shopId: String = "",
    var time: Long = 0
):Parcelable