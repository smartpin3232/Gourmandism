package com.louis.gourmandism.data.source

import androidx.lifecycle.MutableLiveData
import com.louis.gourmandism.data.*

interface DataSource {

    suspend fun getComment(userId: String, mode: Int): Result<List<Comment>>

    suspend fun newComment(comment: Comment): Result<Boolean>

    suspend fun setLike(commentId: String, userId: String, status: Int): Result<Boolean>

    suspend fun joinGame(eventId: String, userId: String, status: Int): Result<Boolean>

    suspend fun getShop(id: String, mode: Int): Result<List<Shop>>

    suspend fun newShop(shop: Shop): Result<Boolean>

    suspend fun getEvent(status: Int): Result<List<Event>>

    suspend fun newEvent(event: Event): Result<Boolean>

    suspend fun createUser(user: User): Result<Boolean>

    suspend fun getUser(id: String): Result<User>

    suspend fun getMyFavorite(userId: String): Result<MutableList<Favorite>>

    suspend fun setWish(folderId: String, shopId: String, status: Int) : Result<Boolean>

    suspend fun newWishList(favorite: Favorite): Result<Boolean>

    fun getLiveComments(): MutableLiveData<List<Comment>>

    fun getLiveEvents(status: Int): MutableLiveData<List<Event>>

    suspend fun setBrowserHistory(userId: String, browseRecently: BrowseRecently): Result<Boolean>

    suspend fun setSelectTag(userId: String, tag: String): Result<Boolean>
}