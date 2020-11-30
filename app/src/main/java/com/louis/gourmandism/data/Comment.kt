package com.louis.gourmandism.data

data class Comment(
    var id: String = "",
    var shopId: String = "",
    var images: MutableList<String>? = null,
    var title: String = "",
    var star: Float = 0.toFloat(),
    var content: String = "",
    var like: MutableList<String>? = null, //userId
    var createdTime : com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
)