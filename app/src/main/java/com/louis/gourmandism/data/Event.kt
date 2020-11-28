package com.louis.gourmandism.data

data class Event(
    var id: String,
    var hostId: String,
    var title: String,
    var content: String,
    var shopId: String,
    var status: Int,
    var createTime: Long,
    var member: MutableList<String>
)