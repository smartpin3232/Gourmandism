package com.louis.gourmandism.data.source

import androidx.lifecycle.MutableLiveData
import com.louis.gourmandism.data.*

interface Repository {

    suspend fun getComment(userId: String,mode: Int): Result<List<Comment>>

    suspend fun setComment(comment: Comment): Result<Boolean>

    suspend fun getShop(id: String,mode: Int): Result<List<Shop>>

    suspend fun getEvent(status: Int): Result<List<Event>>

    suspend fun getUser(id: String): Result<User>

    suspend fun getMyFavorite(userId: String): Result<MutableList<Favorite>>

    fun getLiveComments(): MutableLiveData<List<Comment>>


}