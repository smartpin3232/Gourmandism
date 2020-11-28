package com.louis.gourmandism.data

data class Shop(
    var id: String,
    var name: String,
    var location: String,
    var openTime: Long,
    var closeTime: Long,
    var phone: Long,
    var image: MutableList<String>,
    var star: Float,
    var type: MutableList<String>
)