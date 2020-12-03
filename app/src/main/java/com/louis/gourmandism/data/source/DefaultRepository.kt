package com.louis.gourmandism.data.source

import androidx.lifecycle.MutableLiveData
import com.louis.gourmandism.data.*
import com.louis.gourmandism.data.source.remote.RemoteDataSource

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation to load Publisher sources.
 */
class DefaultRepository(private val remoteDataSource: DataSource,
                        private val localDataSource: DataSource):Repository{
    override suspend fun getComment(userId: String,mode: Int): Result<List<Comment>> {
        return remoteDataSource.getComment(userId,mode)
    }

    override suspend fun getShop(id: String,mode: Int): Result<List<Shop>> {
        return remoteDataSource.getShop(id,mode)
    }

    override suspend fun getEvent(): Result<List<Event>> {
        return remoteDataSource.getEvent()
    }

    override suspend fun getUser(id: String): Result<User> {
        return remoteDataSource.getUser(id)
    }

    override fun getLiveComments(): MutableLiveData<List<Comment>> {
        return remoteDataSource.getLiveComments()
    }


}
