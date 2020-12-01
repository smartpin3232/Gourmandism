package com.louis.gourmandism.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shop(
    var id: String = "",
    var name: String = "",
    var location: String = "",
    var openTime: Long = 0,
    var closeTime: Long = 0,
    var phone: String = "",
    var image: MutableList<String>? = null,
    var star: Float = 0.toFloat(),
    var type: MutableList<String>? = null
):Parcelable