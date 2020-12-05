package com.louis.gourmandism.data

data class Favorite(
    var user: User,
    var folderName: String,
    var shops: MutableList<Shop>,
    var type: Int, //是否分享 0私有 1共用
    var attentionList: MutableList<String> //userId
)