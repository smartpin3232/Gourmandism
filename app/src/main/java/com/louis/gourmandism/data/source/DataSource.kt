package com.louis.gourmandism.data.source

import androidx.lifecycle.MutableLiveData
import com.louis.gourmandism.data.*

interface DataSource {

    suspend fun getComment(userId: String,mode: Int): Result<List<Comment>>

    suspend fun getShop(id: String,mode: Int): Result<List<Shop>>

    suspend fun getEvent(): Result<List<Event>>

    suspend fun getUser(id: String): Result<User>

    fun getLiveComments(): MutableLiveData<List<Comment>>
}