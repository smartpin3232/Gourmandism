package com.louis.gourmandism.data.source

import androidx.lifecycle.MutableLiveData
import com.louis.gourmandism.data.*

class DefaultRepository(private val remoteDataSource: DataSource,
                        private val localDataSource: DataSource):Repository{
    override suspend fun getComment(userId: String,mode: Int): Result<List<Comment>> {
        return remoteDataSource.getComment(userId, mode)
    }

    override suspend fun newComment(comment: Comment): Result<Boolean> {
        return remoteDataSource.newComment(comment)
    }

    override suspend fun setLike(commentId: String, userId: String, status: Int): Result<Boolean> {
        return remoteDataSource.setLike(commentId, userId, status)
    }

    override suspend fun joinGame(eventId: String,userId: String,status: Int): Result<Boolean> {
        return remoteDataSource.joinGame(eventId, userId, status)
    }

    override suspend fun getShop(id: String,mode: Int): Result<List<Shop>> {
        return remoteDataSource.getShop(id ,mode)
    }

    override suspend fun newShop(shop: Shop): Result<Boolean> {
        return remoteDataSource.newShop(shop)
    }

    override suspend fun getEvent(status: Int): Result<List<Event>> {
        return remoteDataSource.getEvent(status)
    }

    override suspend fun newEvent(event: Event): Result<Boolean> {
        return remoteDataSource.newEvent(event)
    }

    override suspend fun createUser(user: User): Result<Boolean> {
        return remoteDataSource.createUser(user)
    }

    override suspend fun getUser(id: String): Result<User> {
        return remoteDataSource.getUser(id)
    }

    override suspend fun getAllFriend(friendList: List<String>): Result<List<User>> {
        return remoteDataSource.getAllFriend(friendList)
    }

    override suspend fun getMyFavorite(userId: String): Result<MutableList<Favorite>> {
        return remoteDataSource.getMyFavorite(userId)
    }

    override suspend fun setWish(folderId: String, shopId: String, status: Int): Result<Boolean> {
        return remoteDataSource.setWish(folderId, shopId, status)
    }

    override suspend fun newWishList(favorite: Favorite): Result<Boolean> {
        return remoteDataSource.newWishList(favorite)
    }

    override fun getLiveComments(): MutableLiveData<List<Comment>> {
        return remoteDataSource.getLiveComments()
    }

    override fun getLiveEvents(status: Int): MutableLiveData<List<Event>> {
        return remoteDataSource.getLiveEvents(status)
    }

    override suspend fun setBrowserHistory(userId: String, browseRecently: BrowseRecently): Result<Boolean>{
        return remoteDataSource.setBrowserHistory(userId, browseRecently)
    }

    override suspend fun setSelectTag(userId: String, tag: String, status: Boolean): Result<Boolean> {
        return remoteDataSource.setSelectTag(userId, tag, status)
    }

    override suspend fun addFriend(userId: String, friendId: String, status: Boolean): Result<Boolean> {
        return remoteDataSource.addFriend(userId, friendId, status)
    }

    override suspend fun setAttention(userId: String, favoriteId: String, status: Boolean): Result<Boolean> {
        return remoteDataSource.setAttention(userId, favoriteId, status)
    }

    override suspend fun removeWishList(favoriteId: String): Result<Boolean> {
        return  remoteDataSource.removeWishList(favoriteId)
    }

    override suspend fun setShareStatus(favoriteId: String, status: Int): Result<Boolean> {
        return remoteDataSource.setShareStatus(favoriteId, status)
    }


}
