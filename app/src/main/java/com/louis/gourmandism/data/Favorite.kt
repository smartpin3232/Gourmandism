package com.louis.gourmandism.data

data class Favorite(
    var user: User,
    var name: String,
    var shopId: MutableList<String>,
    var type: Boolean
)