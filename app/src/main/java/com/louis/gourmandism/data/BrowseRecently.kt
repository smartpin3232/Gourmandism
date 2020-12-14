package com.louis.gourmandism.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BrowseRecently(
    var shopId: String = "",
    var time: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
):Parcelable