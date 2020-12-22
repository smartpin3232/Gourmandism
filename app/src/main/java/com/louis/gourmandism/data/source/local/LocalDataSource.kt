package com.louis.gourmandism.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.louis.gourmandism.data.*
import com.louis.gourmandism.data.source.DataSource

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation of a Publisher source as a db.
 */
class LocalDataSource(val context: Context) : DataSource {
    override suspend fun getComment(userId: String,mode: Int): Result<List<Comment>> {
        TODO("Not yet implemented")
    }

    override suspend fun newComment(comment: Comment): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setLike(commentId: String, userId: String, status: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun joinGame(eventId: String,userId: String,status: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getShop(id: String,mode: Int): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun newShop(shop: Shop): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(status: Int): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun newEvent(event: Event): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(id: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFriend(friendList: List<String>): Result<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyFavorite(userId: String): Result<MutableList<Favorite>> {
        TODO("Not yet implemented")
    }

    override suspend fun setWish(folderId: String, shopId: String, status: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun newWishList(favorite: Favorite): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLiveComments(): MutableLiveData<List<Comment>> {
        TODO("Not yet implemented")
    }

    override fun getLiveEvents(status: Int): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun setBrowserHistory(userId: String, browseRecently: BrowseRecently): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setSelectTag(userId: String, tag: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addFriend(userId: String, friendId: String, status: Boolean): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setAttention(
        userId: String,
        favoriteId: String,
        status: Boolean
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }


}
