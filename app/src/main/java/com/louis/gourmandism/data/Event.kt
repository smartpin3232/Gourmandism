package com.louis.gourmandism.data

data class Event(
    var id: String = "",
    var host: User? = null,
    var title: String = "",
    var content: String = "",
    var shopId: String = "",
    var status: Int = 0,
    var createTime: Long= 0,
    var member: MutableList<String>? = null
)