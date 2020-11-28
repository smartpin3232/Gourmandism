package com.louis.gourmandism.data

data class User(
    var id: String = "",
    var name: String = "",
    var location: String = "",
    var currentPosition: String = "",
    var browseRecently: MutableList<BrowseRecently>? = null,
    var friendList: MutableList<String>? = null
)
