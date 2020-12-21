package com.louis.gourmandism.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shop(
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var location: Location? = null,
    var openTime: Long = 0,
    var closeTime: Long = 0,
    var phone: String = "",
    var image: MutableList<String>? = null,
    var star: Float = 0.toFloat(),
    var type: MutableList<String>? = null
):Parcelable

@Parcelize
data class Location(
    var locationX: Double = 0.0,
    var locationY: Double = 0.0
):Parcelable

@Parcelize
data class OpenTime(
    var day: String = "",
    var startTime: String = "",
    var endTime: String = ""
):Parcelable