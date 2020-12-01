package com.louis.gourmandism.data.source

import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.Result
import com.louis.gourmandism.data.Shop

interface Repository {

    suspend fun getComment(): Result<List<Comment>>

    suspend fun getShop(id: String): Result<List<Shop>>
}