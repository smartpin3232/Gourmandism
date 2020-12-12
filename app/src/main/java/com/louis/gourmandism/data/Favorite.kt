package com.louis.gourmandism.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    var id: String = "",
    var userId: String = "",
    var userName: String = "",
    var folderName: String = "",
    var shops: MutableList<String>? = null,
    var shopsInfo: MutableList<Shop>? = null,
    var type: Int = 0, //是否分享 0私有 1共用
    var attentionList: MutableList<String>? = null //userId
): Parcelable