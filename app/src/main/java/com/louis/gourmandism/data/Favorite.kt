package com.louis.gourmandism.data

data class Favorite(
    var userId: String,
    var name: String,
    var shopId: MutableList<String>,
    var type: Boolean
)