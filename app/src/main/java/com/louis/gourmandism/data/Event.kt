package com.louis.gourmandism.data

data class Event(
    var id: String = "",
    var host: User? = null,
    var title: String = "",
    var content: String = "",
    var shop: Shop? = null,
    var status: Int = 0, //status   0: 未開團 , 1: 已完成
    var createTime: Long = 0,
    var member: MutableList<String>? = null,
    var memberLimit: Int = 0,
    var time: Long = 0
)